#
# Copyright 2022-2024 NXP
#

require recipes-fsl/images/fsl-image-auto.bb

# Add packages common between dom0 and other domUs.
IMAGE_INSTALL += " \
    ${PACKAGES-CORE-benchmark} \
    iptables \
    ifmetric \
    strongswan \
    ntp ntpq \
    python3-pip \
    openjdk-8 \
    cronie \
    resolvconf \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'pfe-slave', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'optee', 'optee-examples-goldvip', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'k3s k3s-airgap-pause-container', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-crypto', 'opensc p11-kit', '', d)} \
"

update_timestamp() {
    echo "$(date -u +%4Y%2m%2d%2H%2M%2S)" > ${IMAGE_ROOTFS}/etc/timestamp
}

ROOTFS_POSTPROCESS_COMMAND += "update_timestamp; "

# Use the ifup/ifdown services included in busybox.
BAD_RECOMMENDATIONS += "ifupdown"

