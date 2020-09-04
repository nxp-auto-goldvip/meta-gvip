#
# Copyright 2020 NXP
#

DESCRIPTION = "Gateway-VIP Image"

require recipes-fsl/images/fsl-image-auto.bb

# Add GVIP required packages
IMAGE_INSTALL += " \
    ${PACKAGES-CORE-benchmark} \
    python-pip \
    python3-pip \
"
