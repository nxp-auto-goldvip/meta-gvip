#
# Copyright 2022 NXP
#

SUMMARY = "Package GoldVIP OTAmatic Client app container image"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

GOLDVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

# Path where the k3s expects the air-gapped images.
IMAGES_DIR = "/var/lib/rancher/k3s/agent/images/"
# Path for auto-deploying manifests.
MANIFESTS_DIR = "/var/lib/rancher/k3s/server/manifests"

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

    install -d ${D}/${MANIFESTS_DIR}
    install -m 0644 ${S}/containers/manifests/otamatic.yaml ${D}/${MANIFESTS_DIR}/

    install -d ${D}/${DESTDIR}
    ln -sr ${D}/${MANIFESTS_DIR}/otamatic.yaml ${D}/${DESTDIR}/otamatic.yaml
}

FILES_${PN} += " \
    ${IMAGES_DIR} \
    ${MANIFESTS_DIR} \
    ${DESTDIR} \
"
