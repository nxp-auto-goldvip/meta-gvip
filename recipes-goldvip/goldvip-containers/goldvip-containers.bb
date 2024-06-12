SUMMARY = "Gold VIP (Vehicle Integration Platform) containers support for dom0"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

require include/goldvip-containers.inc

S = "${WORKDIR}/git"

do_install:append() {
    install -d ${D}/${K3S_CONFIG_DIR}
    install -m 0644 ${S}/containers/conf/no-hv/config-server.yaml ${D}/${K3S_CONFIG_DIR}

    install -d ${D}/${DESTDIR}

    # Example manifest, deployed by user.
    install -m 0644 ${S}/containers/manifests/no-hv/nginx.yaml ${D}/${DESTDIR}/
}

FILES:${PN} += " \
    ${DESTDIR} \
    ${K3S_CONFIG_DIR} \
"
