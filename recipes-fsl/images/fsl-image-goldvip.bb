#
# Copyright 2020-2023 NXP
#

DESCRIPTION = "GoldVIP Image"

include ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'conf/machine/goldvip-s32g-domu.conf', '', d)}

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
IMAGE_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-benchmark', 'goldvip-benchmark', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-crypto', 'pkcs11-hse p11-kit-hse', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'goldvip-gateway', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'aws-iot-fleetwise-edge', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-ml', 'goldvip-ml', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-cloud-gw-dom0', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-xen', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-containerization', 'goldvip-containers-dom0', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'goldvip-ota-agents-demo goldvip-remote-ua-demo', '', d)} \
"

# Select the k3s and OTA Update Agents packages based on DISTRO_FEATURES content.
python() {
    # Choose the k3s node type based on virtualization usage (if xen is enabled, then v2xdomu
    # acts as a master node and dom0 can start an agent)
    if bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', True, False, d):
        d.appendVar('IMAGE_INSTALL', bb.utils.contains('DISTRO_FEATURES', 'xen',
                                                       ' k3s-agent', ' k3s-server', d))
    if bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', True, False, d):
        d.appendVar('IMAGE_INSTALL', \
                    bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', \
                                      ' goldvip-ota-agents-container', ' goldvip-ota-agents', d))
        d.appendVar('IMAGE_INSTALL', \
                    bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', \
                                      ' goldvip-remote-ua-container', ' goldvip-remote-ua', d))
}

# add additional binaries in SD-card FAT partition
SDCARDIMAGE_BOOT_EXTRA_FILES_append += "arm-trusted-firmware:fip.s32-sdcard "
SDCARDIMAGE_BOOT_EXTRA_FILES_append += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'goldvip-gateway:goldvip-gateway.bin', '', d)}"
SDCARDIMAGE_BOOT_EXTRA_FILES_append += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader:boot-loader', '', d)}"

# This image shall have a size of 2.5GiB with at least 700MiB of free space. To achieve that, set the
# maximum size to 2.5GiB and define the expected extra space. IMAGE_ROOTFS_SIZE is the difference
# between the aforementioned values. The build fails if the image will exceed the 2.5GiB limit.
IMAGE_ROOTFS_MAXSIZE = "2621440"
IMAGE_ROOTFS_EXTRA_SPACE = "716800"

# Set image overhead factor to 1, as extra space is guaranteeed by the IMAGE_ROOTFS_EXTRA_SPACE variable
IMAGE_OVERHEAD_FACTOR = "1"

# Unfortunately, only IMAGE_ROOTFS_EXTRA_SPACE is evaluated in image.bbclass, so this must be
# calculated statically.
IMAGE_ROOTFS_SIZE = "1904640"
