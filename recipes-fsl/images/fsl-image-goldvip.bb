# Copyright 2020-2025 NXP

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
    libfci-cli \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'init-ifupdown-dom0', 'init-ifupdown-linux', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-cloud', 'goldvip-cloud-gw-dom0', '', d)} \
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
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway goldvip-dds', 'goldvip-dds', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-ml', 'goldvip-ml', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'goldvip-xen', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-ota', 'goldvip-ota-agents-demo goldvip-remote-ua-demo', '', d)} \
"

python() {
    # Select the k3s and OTA Update Agents packages based on DISTRO_FEATURES content.
    # Choose the k3s node type based on virtualization usage (if xen is enabled, then v2xdomu
    # acts as a master node and dom0 can start an agent)
    if bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', True, False, d):
        d.appendVar('IMAGE_INSTALL', bb.utils.contains('DISTRO_FEATURES', 'xen',
                                                       ' k3s-agent goldvip-containers-dom0', ' k3s-server goldvip-containers', d))
    if bb.utils.contains('DISTRO_FEATURES', 'goldvip-ota', True, False, d):
        d.appendVar('IMAGE_INSTALL', \
                    bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', \
                                      ' goldvip-remote-ua-container goldvip-ota-agents-container' ,
                                      ' goldvip-remote-ua goldvip-ota-agents', d))

    # If virtualization is missing add the optional packages that are supposed to be on domU
    if bb.utils.contains('DISTRO_FEATURES', 'xen', False, True, d):
        d.appendVar('IMAGE_INSTALL', bb.utils.contains('DISTRO_FEATURES', 'goldvip-adaptive-autosar', ' eb-ara', '', d))
        d.appendVar('IMAGE_INSTALL', bb.utils.contains('DISTRO_FEATURES', 'goldvip-telemetry-server', ' goldvip-telemetry-server', '', d))
        d.appendVar('IMAGE_INSTALL', bb.utils.contains('DISTRO_FEATURES', 'goldvip-ota', ' goldvip-ota-client-demo', '', d))
        d.appendVar('IMAGE_INSTALL', bb.utils.contains('DISTRO_FEATURES', 'goldvip-cloud', ' goldvip-cloud-gw-domu', '', d))
        if bb.utils.contains('DISTRO_FEATURES', 'goldvip-ota', True, False, d):
            d.appendVar('IMAGE_INSTALL', \
                    bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', \
                                      ' goldvip-ota-client-container', ' goldvip-ota-client', d))
}

# add additional binaries in SD-card FAT partition
SDCARDIMAGE_BOOT_EXTRA_FILES:append = " arm-trusted-firmware:bl2_w_dtb.s32-sdcard "
SDCARDIMAGE_BOOT_EXTRA_FILES:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'goldvip-gateway:goldvip-gateway.bin', '', d)}"
SDCARDIMAGE_BOOT_EXTRA_FILES:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'goldvip-bootloader:boot-loader', '', d)}"
python() {
    if (oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', True, False, d) and
        bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', True, False, d)):
        d.appendVar('SDCARDIMAGE_BOOT_EXTRA_FILES', ' goldvip-bootloader:Bootloader_Configuration.bin')
}

# Set the size of the free space allocated in this image:
# - in environments with hypervisor (Xen is enabled), add 1 GiB of free space. This means that the
#   rootfs image for dom0 shall have a size of around 3 GiB.
# - in environments without hypervisor (standalone Linux), add 2200 MiB of free space. This leads to
#   a rootfs image of 4 GiB.
# The builds will fail if the rootfs size exceeds the configured sizes (either 3 GiB or 4 GiB).
# Note: All the sizes are aligned to 4096 (check the value of the IMAGE_ROOTFS_ALIGNMENT).
IMAGE_ROOTFS_MAXSIZE = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '3145728', '4194304', d)}"
IMAGE_ROOTFS_EXTRA_SPACE = "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '1048576', '2252800', d)}"

# Only IMAGE_ROOTFS_EXTRA_SPACE is evaluated in image bbclass, so this must be calculated
# statically (IMAGE_ROOTFS_SIZE = IMAGE_ROOTFS_MAXSIZE - IMAGE_ROOTFS_EXTRA_SPACE).
IMAGE_ROOTFS_SIZE = "2097152"

# Set image overhead factor to 1, as extra space is guaranteeed by the IMAGE_ROOTFS_EXTRA_SPACE variable
IMAGE_OVERHEAD_FACTOR = "1"

