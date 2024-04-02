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
# Path for auto-deploying manifests.
MANIFESTS_DIR = "/var/lib/rancher/k3s/server/manifests"

S = "${WORKDIR}/git"
DESTDIR = "/home/root/containers"

RDEPENDS:${PN} += "k3s"
