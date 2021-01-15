#
# Copyright 2021 NXP
#

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DELTA_KERNEL_DEFCONFIG_append += " greengrass.cfg"
SRC_URI_append = " file://build/greengrass.cfg"
