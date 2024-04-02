# Copyright 2020-2023 NXP

DESCRIPTION = "GoldVIP domU image"

require recipes-fsl/images/fsl-image-goldvip-base.inc

# Remove some unnecessary packages added by DISTRO_FEATURES options or from
# the fsl-image-auto image.
IMAGE_INSTALL:remove = " \
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
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-containerization', 'goldvip-containers-v2xdomu k3s-server', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-crypto', 'p11-kit-hse-remote', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'goldvip-ota-client-demo', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-telemetry-server', 'goldvip-telemetry-server', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-adaptive-autosar', 'eb-ara', '', d)} \
"

# Select the OTA client package based on DISTRO_FEATURES.
python() {
    if bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', True, False, d):
        d.appendVar('IMAGE_INSTALL', \
                    bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', \
                                      ' goldvip-ota-client-container', ' goldvip-ota-client', d))
}

# Disable OpenSSL crypto offload for v2xdomu VM, since it doesn't have access to HSE.
disable_crypto_offload() {
    sed -i 's/^openssl_conf = goldvip_openssl_def/#&/' ${IMAGE_ROOTFS}${sysconfdir}/ssl/openssl.cnf
}

# Update hostname to v2xdomu.
update_hostname() {
    GOLDVIP_DOMU_HOSTNAME="v2xdomu"
    echo "${GOLDVIP_DOMU_HOSTNAME}" > ${IMAGE_ROOTFS}${sysconfdir}/hostname
}
ROOTFS_POSTPROCESS_COMMAND += "update_hostname; disable_crypto_offload; "

# The domU partition will be increased after the first GoldVIP image boot.
# Allow yocto to add some extra space required for an initial boot based on
# the default overhead factor (IMAGE_OVERHEAD_FACTOR). Avoid adding extra space
# here (IMAGE_ROOTFS_EXTRA_SPACE), unless necessary.
IMAGE_ROOTFS_EXTRA_SPACE = "0"
