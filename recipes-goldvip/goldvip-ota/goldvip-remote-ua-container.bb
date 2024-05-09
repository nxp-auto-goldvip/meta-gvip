#
# Copyright 2022-2023 NXP
#

SUMMARY = "Package GoldVIP  Remote Update Agent container image"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

# Path where the k3s expects the air-gapped images.
IMAGES_DIR = "/var/lib/rancher/k3s/agent/images/"

# Basename of the OTA client OCI image.
CONTAINER_OCI_IMG = "${PN}-image-${MACHINE}.oci-image.tar"

do_install[depends] += "${PN}-image:do_image_complete"
do_compile[noexec] = "1"

do_install () {
    install -d ${D}/${IMAGES_DIR}
    install -m 0644 ${DEPLOY_DIR_IMAGE}/${CONTAINER_OCI_IMG} ${D}${IMAGES_DIR}
}

FILES:${PN} += " \
    ${IMAGES_DIR} \
"
