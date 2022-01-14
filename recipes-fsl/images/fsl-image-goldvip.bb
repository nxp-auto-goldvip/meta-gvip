#
# Copyright 2020-2021 NXP
#

DESCRIPTION = "GoldVIP Image"

include ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'conf/machine/goldvip-${MACHINE}-domu.conf', '', d)}
include ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'conf/machine/goldvip-${MACHINE}-ota.conf', '', d)}

require recipes-fsl/images/fsl-image-auto.bb

# Add GoldVIP required packages
IMAGE_INSTALL += " \
    ${PACKAGES-CORE-benchmark} \
    iptables \
    dnsmasq  \
    ntp ntpq \
    python-pip \
    python3-pip \
    python3-mmap \
    python3-fcntl \
    openjdk-8 \
    goldvip-apps \
    packagegroup-base-wifi \
    ifmetric \
    linux-firmware-rtlwifi \
"

# Allow builds without XEN enabled
GOLDVIP_DOMU_ROOTFS_SOURCE ?= ""

do_image_sdcard[depends] += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${GOLDVIP_DOMU_ROOTFS_SOURCE}:do_image_complete', '', d)}"

# Add GoldVIP optional packages
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-can-gw', 'goldvip-can-gw', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-xen', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'goldvip-ota-update-agents', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' goldvip-cloud-gw-dom0', '', d)}"

# add additional binaries in SD-card FAT partition
SDCARDIMAGE_BOOT_EXTRA3 = "u-boot"
SDCARDIMAGE_BOOT_EXTRA3_FILE = "u-boot.bin"

SDCARDIMAGE_BOOT_EXTRA4 = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-can-gw', 'goldvip-can-gw', '', d)}"
SDCARDIMAGE_BOOT_EXTRA4_FILE = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-can-gw', 'can-gw.bin', '', d)}"

SDCARDIMAGE_BOOT_EXTRA5 = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader', '', d)}"
SDCARDIMAGE_BOOT_EXTRA5_FILE = "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'boot-loader', '', d)}"

# Reserve some extra space (400MB) for OTA updates.
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', ' + 409600', '', d)}"
