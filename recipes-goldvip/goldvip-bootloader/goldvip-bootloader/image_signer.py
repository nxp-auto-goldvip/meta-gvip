#!/usr/bin/env python3
# =================================================================================================
#
#    @file        image_signer.py
#
#    @brief       Image signer tool. This scripts generates a signed image by
#                 prepending the image signature to the input image
#    @input       File location on disk of a binary image
#    @output      Signed image with the following structure:
#                 For algorithm SHA1/SHA256:
#                     8 Bytes number     Header size(4 bytes)     Image size (4 Bytes)
#                     Image hash (32 bytes maximum)
#                     Header hash (hash of the previous 48 bytes)
#                     Input image
#                 For algorithm RSA 2048:
#                     Image signature 256 bytes
#                     Input image
# =================================================================================================
# @copyright (c)  Copyright 2006-2016 Freescale Semiconductor, Inc. Copyright 2017-2024 NXP
#                 All Rights Reserved.
#
#                 This file contains sample code only.
#                 It is not part of the production code deliverables.
# =================================================================================================

"""
Image signer tool for generating SHA and RSA signatures
"""

import argparse
import hashlib
import os
from Crypto.Hash import SHA256
from Crypto.PublicKey import RSA
from Crypto.Signature.pkcs1_15 import PKCS115_SigScheme


def calculate_hash(bin_data, algorithm):
    """
    Calculate hash of binary data

    Args:
        bin_data (bytes): data to be hashed
        algorithm (str): hashing algorithm (SHA256, SHA1)

    Returns:
        hash_result (str): hash in hex format, padded with zeros until 32 bytes
    """
    signer = hashlib.new(algorithm)
    signer.update(bin_data)
    hash_result = signer.hexdigest()

    hash_result = hash_result + '0' * (64 - len(hash_result))

    return hash_result


def get_image_size(data):
    """
    This method returns image size in hex format

    Args:
        data (bytes): data to be hashed

    Returns:
        image_size (str): image size in hex format, padded with zeros until 8 bytes
    """
    image_size = hex(len(data))
    image_size = image_size[2:]

    image_size = '0' * (8 - len(image_size)) + image_size

    return image_size


def apply_sha_signature(in_file, out_file, algo):
    """
    Apply either SHA1 or SHA256 signature on the input file

    Args:
        in_file (str): Path to the input file to be signed
        out_file (str): Path to the output file containing the signature and input file
        algo (str): Signing algorithm, SHA1 or SHA256

    Returns:
        None
    """
    with open(in_file, 'rb') as input_file:
        image_data = input_file.read()

    # generate image header
    image_header = bytearray.fromhex(os.urandom(8).hex())

    if algo == "SHA256":
        image_header += bytearray.fromhex("00000020")
    else:
        image_header += bytearray.fromhex("00000014")

    image_header += bytearray.fromhex(get_image_size(image_data))
    image_header += bytearray.fromhex(calculate_hash(image_data, algo))

    # sign image header
    header_hash = bytearray.fromhex(calculate_hash(image_header, algo))

    with open(out_file, 'wb') as f:
        f.write(image_header)
        f.write(header_hash)
        f.write(image_data)


def apply_rsa_signature(in_file, out_file, key_file):
    """
    Apply the RSA signature on the input file

    Args:
        in_file (str): Path to the input file to be signed
        out_file (str): Path to the output file containing the signature and input file
        key_file (str): Path to the RSA 2048 private key

    Returns:
        None
    """
    with open(key_file, 'r', encoding='utf-8') as keyfile_handle, \
         open(in_file, 'rb') as image_handle:
        rsa_key = RSA.import_key(keyfile_handle.read())
        input_binary = image_handle.read()

    hash_digest = SHA256.new(input_binary)
    signer = PKCS115_SigScheme(rsa_key)
    signature = signer.sign(hash_digest)

    # generate image header
    with open(out_file, 'wb') as f:
        f.write(input_binary)
        f.write(signature)


def parse_arguments():
    """
    Parse the script arguments

    Returns:
        Namespace object containing current script arguments
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', dest='binary_file', required=True,
                        help='Path to the binary file whose signature is to be calculated')
    parser.add_argument('-o', dest='output_file', required=True, help='Path to the output binary file')
    parser.add_argument('-a', dest='algorithm', required=True, choices=['SHA1', 'SHA256', 'RSA'],
                        help='Image signature algorithm')
    parser.add_argument('-k', dest='key_file', required=False, help='Private key file for RSA algorithm')

    return parser.parse_args()


def main():
    """
    Usage:
    python image_signer.py [-h] -i BINARY_FILE -o OUTPUT_FILE -a {SHA1,SHA256,RSA} [-k KEY_FILE]
    """
    args = parse_arguments()

    if args.algorithm.upper() in ['SHA1', 'SHA256']:
        apply_sha_signature(args.binary_file, args.output_file, args.algorithm.upper())
    elif args.algorithm.upper() == 'RSA':
        apply_rsa_signature(args.binary_file, args.output_file, args.key_file)
    else:
        raise Exception(f'Error: unkown algorithm [{args.algorithm}]')


if __name__ == '__main__':
    main()
