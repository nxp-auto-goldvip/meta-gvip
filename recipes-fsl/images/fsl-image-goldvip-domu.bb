#
# Copyright 2020-2022 NXP
#

DESCRIPTION = "GoldVIP domU image"

require recipes-fsl/images/fsl-image-goldvip-base.inc

# Add GoldVIP required packages
IMAGE_INSTALL += " \
    greengrass-bin \
    goldvip-cloud-gw-domu \
"

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

# Add GoldVIP optional packages for domU image
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-ota', 'goldvip-ota-client', '', d)}"

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
