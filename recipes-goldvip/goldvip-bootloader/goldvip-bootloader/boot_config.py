#!/usr/bin/env python3
# SPDX-License-Identifier: BSD-3-Clause
# -*- coding: utf-8 -*-

"""
This script generates a boot configuration in different formats (json, binary).

Copyright 2024 NXP
"""

import argparse
import json
import os
import re
import struct
import sys
import traceback

#==================================================================================================
# Constant data
#==================================================================================================
MAX_CORE_CONFIGS = 10
CORE_CONFIG_SIZE = 12

MAX_IMAGE_FRAGMENTS = 5
IMAGE_FRAGMENT_SIZE = 24

core_id = {
    "A53_0" : 0, "A53_1" : 1, "A53_2" : 2, "A53_3" : 3,
    "M7_0" : 4, "M7_1" : 5, "M7_2" : 6, "M7_3" : 7,
    "A53_4" : 8, "A53_5" : 9, "A53_6" : 10, "A53_7" : 11
}

algo_dict = {
    "NONE" : 0,
    "SHA1" : 1,
    "SHA256" : 2,
    "CRC32" : 3
}

source_dict = {
    "UNKNOWN" : 0,
    "QSPI" : 1,
    "SDMMC" : 2,
    "COM" : 3
}

#==================================================================================================
# Global functions
#==================================================================================================
def convert_to_bytes(var, size):
    """
    Converts Python values to C-language binary data using the little-endian byte ordering
    :param var: variable to be converted
    :param size: variable storage size
    """
    size_format = {1: '<B', 2: '<H', 4: '<L'}

    if size in size_format:
        return bytearray(struct.pack(size_format[size], var))

    print("Error: Invalid size for convert_to_bytes")
    sys.exit(-1)

def convert_to_zeros(size):
    """
    Generate zeros bytes for padding
    :param size: number of padding bytes
    """
    return bytearray([0x0]) * size

def generate_core_config(core):
    """
    Generate binary configuration of a core
    :param core: core configuration object
    """
    core_binary = bytearray()

    core_binary += convert_to_bytes(core_id[core['CoreID']], 1)
    core_binary += convert_to_bytes(source_dict[core['ImageStorage']], 1)
    core_binary += convert_to_bytes(int(core['DecryptionKey']), 1)
    core_binary += convert_to_zeros(1) # padding
    core_binary += convert_to_bytes(int(core['ResetHandler'], 16), 4)
    core_binary += convert_to_bytes(algo_dict[core['AuthAlgorithm']], 1)
    core_binary += convert_to_bytes(len(core['ImageFragments']), 1)
    core_binary += convert_to_bytes(int(core['IsCriticalApp'].lower()=='true'), 1)
    core_binary += convert_to_bytes(int(core['StartCore'].lower()=='true'), 1)

    return core_binary

def generate_fragment_config(fragment):
    """
    Generate binary configuration of an image fragment
    :param fragment: fragment configuration object
    """
    fragment_binary = bytearray()

    fragment_binary += convert_to_bytes(int(fragment['FlashAddress'], 16), 4)
    fragment_binary += convert_to_bytes(0xFFFFFFFF, 4) # invalid com session ID
    fragment_binary += convert_to_bytes(int(fragment['RamAddress'], 16), 4)
    fragment_binary += convert_to_bytes(int(fragment['FragmentSize']), 4)
    fragment_binary += convert_to_bytes(int(fragment['CrcValue'], 16), 4)
    fragment_binary += convert_to_bytes(int(fragment['MemDalBlockId'], 16), 2)
    fragment_binary += convert_to_bytes(int(fragment['SmrIndex']), 1)
    fragment_binary += convert_to_zeros(1) # padding

    return fragment_binary

def apply_custom_configuration(cfg, custom_variables):
    """
    Update the default json configuration based on a list of custom variables.
    The custom variable format will be: Core-<index> [Image-<index>] <name>=<value>
    :param cfg: default configuration
    :param custom_variables: list of custom variables which overrides the default ones
    Examples of variables:
        Core-0 ResetHandler=0x34700000
        Core-0 ImageStorage=SDMMC
        Core-1 Image-0 RamAddress=0x34200000
    """

    for var in custom_variables:
        core = re.search("(?<=Core-)\\d+", var)
        image = re.search("(?<=Image-)\\d+", var)

        core_index = int(core.group(0)) if core else None
        image_index = int(image.group(0)) if image else None

        var_name = var.split()[-1].split('=')[0]
        var_value = var.split()[-1].split('=')[1]

        try:
            core_cfg = cfg['CoreConfiguration'][core_index]
        except (IndexError, TypeError):
            print(f'Error: Invalid core index in custom variable "{var}"')
            continue

        if image_index is None:
            # This is a core configuration
            core_cfg[var_name] = var_value
        else:
            # This is an image configuration
            if image_index < len(core_cfg['ImageFragments']):
                core_cfg['ImageFragments'][image_index][var_name] = var_value
            else:
                print(f'Error: Invalid image index in custom variable "{var}"')

def parse_arguments():
    """
    Parse the script arguments
    """
    parser = argparse.ArgumentParser()

    parser.add_argument('-g', '--generate_path', required=True,
                        help='Path to the output directory where files are generated')
    parser.add_argument('-i', '--input_file', required=False, default='Bootloader_Configuration.json',
                        help='Name of the input json configuration file')
    parser.add_argument('-o', '--output_file', required=False, default='Bootloader_Configuration.bin',
                        help='Name of the output binary configuration file')
    parser.add_argument('-v', dest='custom_vars', required=False, nargs='+', default=[],
                        help='Optional list of custom configuration variables')

    return parser.parse_args()

def main():
    """
    Generate a binary Bootloader configuration
    """
    args = parse_arguments()

    input_file_path = os.path.join(args.generate_path, args.input_file)
    output_file_path = os.path.join(args.generate_path, args.output_file)

    boot_cfg = None
    with open(input_file_path, encoding="utf-8") as stream:
        boot_cfg = json.load(stream)

    if not boot_cfg['General']['DynamicConfigEnabled']:
        return

    apply_custom_configuration(boot_cfg, args.custom_vars)

    binary_config = bytearray()

    # Core configurations number
    binary_config += convert_to_bytes(len(boot_cfg['CoreConfiguration']), 1)
    binary_config += convert_to_zeros(3) # padding

    # Core configurations details
    for core in boot_cfg['CoreConfiguration']:
        binary_config += generate_core_config(core)

    # Fill with zeros the empty core configuration slots
    no_core_configs = MAX_CORE_CONFIGS - len(boot_cfg['CoreConfiguration'])
    binary_config += convert_to_zeros(no_core_configs * CORE_CONFIG_SIZE) # padding

    # Image fragments details
    for core in boot_cfg['CoreConfiguration']:
        for fragment in core['ImageFragments']:
            binary_config += generate_fragment_config(fragment)

        # Fill with zeros the empty image fragments slots
        no_fragment_configs = MAX_IMAGE_FRAGMENTS - len(core['ImageFragments'])
        binary_config += convert_to_zeros(no_fragment_configs * IMAGE_FRAGMENT_SIZE) # padding

    # Fill with zeros the empty image fragments slots where no core is configured
    binary_config += convert_to_zeros(no_core_configs * MAX_IMAGE_FRAGMENTS * IMAGE_FRAGMENT_SIZE)

    with open(output_file_path, "wb") as o:
        o.write(binary_config)

if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print("Error: ", e)
        print("Error: ", traceback.format_exc().replace('\n', '; '))
        sys.exit(-1)
