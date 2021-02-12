#
# Copyright 2020-2021 NXP
#

DESCRIPTION = "Gateway-VIP Image"

require recipes-fsl/images/fsl-image-auto.bb

# Add GVIP required packages
IMAGE_INSTALL += " \
    ${PACKAGES-CORE-benchmark} \
    python-pip \
    python3-pip \
    python3-mmap \
    python3-fcntl \
    openjdk-8 \
    greengrass \
    gvip-apps \
"

# Add GVIP optional packages
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'gvip-can-gw', 'gvip-can-gw', '', d)}"

# add additional binaries in SD-card FAT partition
SDCARDIMAGE_BOOT_EXTRA1 = "u-boot"
SDCARDIMAGE_BOOT_EXTRA1_FILE = "u-boot.bin"
SDCARDIMAGE_BOOT_EXTRA2 = "${@bb.utils.contains('DISTRO_FEATURES', 'gvip-can-gw', 'gvip-can-gw', '', d)}"
SDCARDIMAGE_BOOT_EXTRA2_FILE = "${@bb.utils.contains('DISTRO_FEATURES', 'gvip-can-gw', 'can-gw.bin', '', d)}"
