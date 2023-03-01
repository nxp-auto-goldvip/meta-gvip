#
# Copyright 2022-2023 NXP
#

SUMMARY = "Package GoldVIP OTA Update Agents container image"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=173a22a8dfc7298a74c300ecbfd30c6c"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

# Path where the k3s expects the air-gapped images.
IMAGES_DIR = "/var/lib/rancher/k3s/agent/images/"

# Basename of the OTA client OCI image.
CONTAINER_OCI_IMG = "${PN}-image-${MACHINE}.oci-image.tar"

SRC_URI = " \
    ${GOLDVIP_URL};branch=${GOLDVIP_BRANCH} \
"
SRCREV = "${AUTOREV}"

DESTDIR = "/home/root/containers"
S = "${WORKDIR}/git"

do_install[depends] += "${PN}-image:do_image_complete"
do_compile[noexec] = "1"

do_install () {
    install -d ${D}/${IMAGES_DIR}
    install -m 0644 ${DEPLOY_DIR_IMAGE}/${CONTAINER_OCI_IMG} ${D}${IMAGES_DIR}

    install -d ${D}/${DESTDIR}
    install -m 0644 ${S}/containers/manifests/goldvip-update-agents.yaml ${D}/${DESTDIR}/
}

FILES_${PN} += " \
    ${DESTDIR} \
    ${IMAGES_DIR} \
"
