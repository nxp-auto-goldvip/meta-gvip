#
# Copyright 2020-2021 NXP
#

DESCRIPTION = "GoldVIP Image"

require recipes-fsl/images/fsl-image-auto.bb

# Add GoldVIP required packages
IMAGE_INSTALL += " \
    ${PACKAGES-CORE-benchmark} \
    iptables \
    ntp ntpq \
    python-pip \
    python3-pip \
    python3-mmap \
    python3-fcntl \
    python3-boto3 \
    openjdk-8 \
    greengrass \
    goldvip-apps \
    packagegroup-base-wifi \
    ifmetric \
    linux-firmware-rtlwifi \
"

# Add GoldVIP optional packages
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-can-gw', 'goldvip-can-gw', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-xen', '', d)}"

# add additional binaries in SD-card FAT partition
SDCARDIMAGE_BOOT_EXTRA3 = "u-boot"
SDCARDIMAGE_BOOT_EXTRA3_FILE = "u-boot.bin"

SDCARDIMAGE_BOOT_EXTRA4 = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-can-gw', 'goldvip-can-gw', '', d)}"
SDCARDIMAGE_BOOT_EXTRA4_FILE = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-can-gw', 'can-gw.bin', '', d)}"

SDCARDIMAGE_BOOT_EXTRA5 = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader', '', d)}"
SDCARDIMAGE_BOOT_EXTRA5_FILE = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'boot-loader', '', d)}"

#add more 1GB free space for rootfs
IMAGE_ROOTFS_EXTRA_SPACE = "1048576"
