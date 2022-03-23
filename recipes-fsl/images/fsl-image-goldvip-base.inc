#
# Copyright 2022 NXP
#

require recipes-fsl/images/fsl-image-auto.bb

# Add packages common between dom0 and other domUs.
IMAGE_INSTALL += " \
    ${PACKAGES-CORE-benchmark} \
    iptables \
    ifmetric \
    strongswan \
    ntp ntpq \
    python-pip \
    python3-pip \
    openjdk-8 \
    cronie \
"

update_timestamp() {
    echo "$(date -u +%4Y%2m%2d%2H%2M%2S)" > ${IMAGE_ROOTFS}/etc/timestamp
}

ROOTFS_POSTPROCESS_COMMAND += "update_timestamp; "
