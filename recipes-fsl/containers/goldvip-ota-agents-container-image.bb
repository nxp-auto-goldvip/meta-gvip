# Copyright 2022-2023 NXP

SUMMARY = "Container running the GoldVIP OTA Update Agents"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=eb2d80feeaeafaa489a164e349e12342"

require container-base-image.bb

# This should match the name of the image used in k3s manifest files.
OCI_IMAGE_ANNOTATIONS = "io.containerd.image.name=docker.io/library/goldvip-ota-agents:local"
OCI_IMAGE_TAG = "local"
OCI_IMAGE_ENTRYPOINT = "sh"
OCI_IMAGE_ENTRYPOINT_ARGS = " \
    -c \
    /home/root/ota/goldvip_uas \
"

IMAGE_INSTALL:append = " goldvip-ota-agents"

