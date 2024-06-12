SUMMARY = "Gold VIP (Vehicle Integration Platform) containers support for dom0"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

require include/goldvip-containers.inc

S = "${WORKDIR}/git"

do_install[depends] += "worker-kubeconfig-provision-image:do_image_complete"
do_compile[noexec] = "1"

do_install:append() {
    install -d ${D}/${K3S_CONFIG_DIR}
    install -m 0644 ${S}/containers/conf/hv/config-agent.yaml ${D}/${K3S_CONFIG_DIR}

    install -d ${D}/${IMAGES_DIR}
    install -m 0644 ${DEPLOY_DIR_IMAGE}/worker-kubeconfig-provision-image-${MACHINE}.oci-image.tar ${D}${IMAGES_DIR}
}

FILES:${PN} += " \
    ${IMAGES_DIR} \
    ${K3S_CONFIG_DIR} \
"