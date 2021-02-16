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

SDCARDIMAGE_BOOT_EXTRA1 = "u-boot"
SDCARDIMAGE_BOOT_EXTRA1_FILE = "u-boot.bin"

COMPATIBLE_MACHINE = "s32g274ardb2"
