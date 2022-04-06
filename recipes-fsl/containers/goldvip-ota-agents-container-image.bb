#
# Copyright 2022 NXP
#

SUMMARY = "Container running the GoldVIP OTA Update Agents"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

require container-base-image.bb

# This should match the name of the image used in k3s manifest files.
OCI_IMAGE_ANNOTATIONS = "io.containerd.image.name=docker.io/library/goldvip-ota-agents:local"
OCI_IMAGE_TAG = "local"
OCI_IMAGE_ENTRYPOINT = "sh"
OCI_IMAGE_ENTRYPOINT_ARGS = " \
    -c \
    /home/root/ota/goldvip_uas \
"

IMAGE_INSTALL += " goldvip-ota-agents"
