SUMMARY = "Gold VIP (Vehicle Integration Platform) containers support for dom0"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=66826a98beb097b38ac01d175f237cb4"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-containerization"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI += " \
    ${GOLDVIP_URL};branch=${GOLDVIP_BRANCH} \
"
SRCREV = "${AUTOREV}"

# Path where the k3s expects the air-gapped images.
IMAGES_DIR = "/var/lib/rancher/k3s/agent/images/"
# Path to k3s configuration files.
K3S_CONFIG_DIR = '${sysconfdir}/rancher/k3s'

S = "${WORKDIR}/git"

do_install[depends] += "worker-kubeconfig-provision-image:do_image_complete"
do_compile[noexec] = "1"

RDEPENDS:${PN} += "k3s"

do_install:append() {
    install -d ${D}/${K3S_CONFIG_DIR}
    install -m 0644 ${S}/containers/conf/dom0/* ${D}/${K3S_CONFIG_DIR}

    install -d ${D}/${IMAGES_DIR}
    install -m 0644 ${DEPLOY_DIR_IMAGE}/worker-kubeconfig-provision-image-${MACHINE}.oci-image.tar ${D}${IMAGES_DIR}
}

FILES:${PN} += " \
    ${IMAGES_DIR} \
    ${K3S_CONFIG_DIR} \
"
