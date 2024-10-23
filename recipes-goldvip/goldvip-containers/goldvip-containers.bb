SUMMARY = "Gold VIP (Vehicle Integration Platform) containers support for dom0"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

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
