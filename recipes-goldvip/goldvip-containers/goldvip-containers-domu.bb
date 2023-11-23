SUMMARY = "Gold VIP (Vehicle Integration Platform) containers support for domU"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=fafa1a4becd2f1c0df63f867ef81796a"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-containerization"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI += " \
    ${GOLDVIP_URL};branch=${GOLDVIP_BRANCH};name=goldvip \
"
SRCREV_goldvip = "${AUTOREV}"

# Path for auto-deploying manifests.
MANIFESTS_DIR = "/var/lib/rancher/k3s/server/manifests"
# Path to k3s configuration files.
K3S_CONFIG_DIR = '${sysconfdir}/rancher/k3s'

S = "${WORKDIR}/git"
DESTDIR = "/home/root/containers"

RDEPENDS:${PN} += "k3s"

do_install:append() {
    install -d ${D}/${K3S_CONFIG_DIR}
    install -m 0644 ${S}/containers/conf/domu/* ${D}/${K3S_CONFIG_DIR}

    install -d ${D}/${MANIFESTS_DIR}
    install -d ${D}/${DESTDIR}

    # Example manifest, deployed by user.
    install -m 0644 ${S}/containers/manifests/nginx.yaml ${D}/${DESTDIR}/

    install -m 0644 ${S}/containers/manifests/worker-kubeconfig-provision.yaml ${D}/${MANIFESTS_DIR}/
    ln -sr ${D}/${MANIFESTS_DIR}/worker-kubeconfig-provision.yaml ${D}/${DESTDIR}/worker-kubeconfig-provision.yaml

    if ${@bb.utils.contains('DISTRO_FEATURES', 'xen goldvip-ota', 'true', 'false', d)}; then
        install -m 0644 ${S}/containers/manifests/goldvip-update-agents.yaml ${D}/${MANIFESTS_DIR}/
        ln -sr ${D}/${MANIFESTS_DIR}/goldvip-update-agents.yaml ${D}/${DESTDIR}/goldvip-update-agents.yaml
    fi
}

FILES:${PN} += " \
    ${DESTDIR} \
    ${K3S_CONFIG_DIR} \
    ${MANIFESTS_DIR} \
"
