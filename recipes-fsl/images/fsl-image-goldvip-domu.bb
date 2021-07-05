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
    openjdk-8 \
    greengrass \
    goldvip-cloud-gw-domu \
    packagegroup-base-wifi \
    ifmetric \
    linux-firmware-rtlwifi \
"

#add more 1GB free space for rootfs
IMAGE_ROOTFS_EXTRA_SPACE = "1048576"
