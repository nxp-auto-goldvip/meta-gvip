SUMMARY = "Gold VIP (Vehicle Integration Platform) containers support for domU"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

require include/goldvip-containers.inc

S = "${WORKDIR}/git"

do_install:append() {
    install -d ${D}/${K3S_CONFIG_DIR}
    install -m 0644 ${S}/containers/conf/hv/config-server.yaml ${D}/${K3S_CONFIG_DIR}

    install -d ${D}/${MANIFESTS_DIR}
    install -d ${D}/${DESTDIR}

    # Example manifest, deployed by user.
    install -m 0644 ${S}/containers/manifests/hv/nginx.yaml ${D}/${DESTDIR}/

    install -m 0644 ${S}/containers/manifests/hv/worker-kubeconfig-provision.yaml ${D}/${MANIFESTS_DIR}/
    ln -sr ${D}/${MANIFESTS_DIR}/worker-kubeconfig-provision.yaml ${D}/${DESTDIR}/worker-kubeconfig-provision.yaml

    if ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'true', 'false', d)}; then
        install -m 0644 ${S}/containers/manifests/hv/goldvip-update-agents.yaml ${D}/${MANIFESTS_DIR}/
        ln -sr ${D}/${MANIFESTS_DIR}/goldvip-update-agents.yaml ${D}/${DESTDIR}/goldvip-update-agents.yaml
    fi
}

FILES:${PN} += " \
    ${DESTDIR} \
    ${K3S_CONFIG_DIR} \
    ${MANIFESTS_DIR} \
"
