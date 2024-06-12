#
# Copyright 2022-2023 NXP
#

SUMMARY = "Package GoldVIP OTA Update Agents container image"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
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

    install -d ${D}/${DESTDIR}
    install -d ${D}/${MANIFESTS_DIR}

    install -m 0644 ${S}/containers/manifests/${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'hv', 'no-hv', d)}/goldvip-update-agents.yaml ${D}/${MANIFESTS_DIR}/
    ln -sr ${D}/${MANIFESTS_DIR}/goldvip-update-agents.yaml ${D}/${DESTDIR}/goldvip-update-agents.yaml
}

FILES:${PN} += " \
    ${DESTDIR} \
    ${IMAGES_DIR} \
    ${MANIFESTS_DIR} \
"
