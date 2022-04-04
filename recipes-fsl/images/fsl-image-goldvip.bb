#
# Copyright 2020-2022 NXP
#

DESCRIPTION = "GoldVIP Image"

include ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'conf/machine/goldvip-${MACHINE}-domu.conf', '', d)}

require recipes-fsl/images/fsl-image-goldvip-base.inc

# Add GoldVIP required packages.
IMAGE_INSTALL += " \
    dnsmasq  \
    goldvip-sdcard-partitioning \
    goldvip-apps \
    kernel-module-ipc-chardev \
    packagegroup-base-wifi \
    linux-firmware-rtlwifi \
    nxp-wlan-sdk \
    kernel-module-nxp89xx \
    linux-firmware-nxp89xx \
"

# Allow builds without XEN enabled.
GOLDVIP_DOMU_ROOTFS_SOURCE ?= ""

do_image_sdcard[depends] += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${GOLDVIP_DOMU_ROOTFS_SOURCE}:do_image_complete', '', d)}"

# Add GoldVIP optional packages.
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'goldvip-gateway', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-xen', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'goldvip-ota-update-agents', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-cloud-gw-dom0', '', d)}"

# add additional binaries in SD-card FAT partition
SDCARDIMAGE_BOOT_EXTRA_FILES_append += "arm-trusted-firmware:fip.s32-sdcard "
SDCARDIMAGE_BOOT_EXTRA_FILES_append += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'goldvip-gateway:goldvip-gateway.bin', '', d)}"
SDCARDIMAGE_BOOT_EXTRA_FILES_append += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader:boot-loader', '', d)}"

# This image shall have a size of 2GiB with at least 700MiB of free space. To achieve that, set the
# maximum size to 2GiB and define the expected extra space. IMAGE_ROOTFS_SIZE is the difference
# between the aforementioned values. The build fails if the image will exceed the 2GiB limit.
IMAGE_ROOTFS_MAXSIZE = "2097152"
IMAGE_ROOTFS_EXTRA_SPACE = "716800"

# Set image overhead factor to 1, as extra space is guaranteeed by the IMAGE_ROOTFS_EXTRA_SPACE variable
IMAGE_OVERHEAD_FACTOR = "1"

# Unfortunately, only IMAGE_ROOTFS_EXTRA_SPACE is evaluated in image.bbclass, so this must be
# calculated statically.
IMAGE_ROOTFS_SIZE = "1380352"
