#
# Copyright 2020-2021 NXP
#

DESCRIPTION = "GoldVIP domU image"

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
    python3-scapy \
    python3-requests \
    aws-iot-device-sdk-python-v2 \
    openjdk-8 \
    greengrass-bin \
    goldvip-cloud-gw-domu \
    packagegroup-base-wifi \
    ifmetric \
    linux-firmware-rtlwifi \
    sudo \
"

# Update hostname to v2xdomu
update_hostname() {
    GOLDVIP_DOMU_HOSTNAME="v2xdomu"
    echo "${GOLDVIP_DOMU_HOSTNAME}" > ${IMAGE_ROOTFS}${sysconfdir}/hostname
}
ROOTFS_POSTPROCESS_COMMAND += "update_hostname; "

# Add GoldVIP optional packages for domU image
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'goldvip-ota-client', '', d)}"

#add more 1GB free space for rootfs
IMAGE_ROOTFS_EXTRA_SPACE = "1048576"
