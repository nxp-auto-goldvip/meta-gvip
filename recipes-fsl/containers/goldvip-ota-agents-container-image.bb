# Copyright 2022-2024 NXP

require recipes-fsl/containers/container-base-image.inc

SUMMARY = "Container running the GoldVIP OTA Update Agents"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

# This should match the name of the image used in k3s manifest files.
OCI_IMAGE_ANNOTATIONS = "io.containerd.image.name=docker.io/library/goldvip-ota-agents:local"
OCI_IMAGE_TAG = "local"
OCI_IMAGE_ENTRYPOINT = "sh"
OCI_IMAGE_ENTRYPOINT_ARGS = " \
    -c \
    /root/ota/goldvip_uas \
"

IMAGE_INSTALL:append = " goldvip-ota-agents"

