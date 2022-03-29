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
"
