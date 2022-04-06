#
# Copyright 2020-2022 NXP
#

DESCRIPTION = "GoldVIP domU image"

require recipes-fsl/images/fsl-image-goldvip-base.inc

# Remove some unnecessary packages added by DISTRO_FEATURES options or from
# the fsl-image-auto image.
IMAGE_INSTALL_remove += " \
   zlib-dev \
   xen-tools-xenstat \
   xen-tools-xenmon \
   ${XEN_KERNEL_MODULES} \
   xen-tools \
   qemu \
   sja1110 \
"

IMAGE_INSTALL += " \
    goldvip-cloud-gw-domu \
    greengrass-bin \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'goldvip-ota-client-demo', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-containerization', 'goldvip-containers-domu k3s-server', '', d)} \
"

# Select the OTA client package based on DISTRO_FEATURES.
python() {
    if bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', True, False, d):
        d.appendVar('IMAGE_INSTALL', \
                    bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', \
                                      ' goldvip-ota-client-container', ' goldvip-ota-client', d))
}

# Update hostname to v2xdomu.
update_hostname() {
    GOLDVIP_DOMU_HOSTNAME="v2xdomu"
    echo "${GOLDVIP_DOMU_HOSTNAME}" > ${IMAGE_ROOTFS}${sysconfdir}/hostname
}
ROOTFS_POSTPROCESS_COMMAND += "update_hostname; "

# The domU partition will be increased after the first GoldVIP image boot.
# Allow yocto to add some extra space required for an initial boot based on
# the default overhead factor (IMAGE_OVERHEAD_FACTOR). Avoid adding extra space
# here (IMAGE_ROOTFS_EXTRA_SPACE), unless necessary.
IMAGE_ROOTFS_EXTRA_SPACE = "0"
