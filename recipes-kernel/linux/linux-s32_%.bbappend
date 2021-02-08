#
# Copyright 2021 NXP
#

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# add AWS Greengrass required kernel configs
DELTA_KERNEL_DEFCONFIG_append += " greengrass.cfg"
SRC_URI_append = " file://build/greengrass.cfg"

# add GVIP required kernel configs
DELTA_KERNEL_DEFCONFIG_append += " gvip.cfg"
SRC_URI_append = " file://build/gvip.cfg"
