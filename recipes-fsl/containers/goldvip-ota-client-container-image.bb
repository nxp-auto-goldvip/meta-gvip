# Copyright 2022-2023 NXP

SUMMARY = "Container running the OTAmatic Client for GoldVIP"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=66826a98beb097b38ac01d175f237cb4"

require container-base-image.bb

# This should match the name of the image used in k3s manifest files.
OCI_IMAGE_ANNOTATIONS = "io.containerd.image.name=docker.io/library/goldvip-ota-client:local"
OCI_IMAGE_TAG = "local"
OCI_IMAGE_ENTRYPOINT = "sh"
OCI_IMAGE_ENTRYPOINT_ARGS = " \
    -c \
    otamatic_sample_app \
"

IMAGE_INSTALL:append = " goldvip-ota-client"

