ARTIFACTS_DIR = "${WORKDIR}/goldvip_uas"

require recipes-goldvip/goldvip-ota/goldvip-ota-agents.inc

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_UPDATE_AGENTS_TARBALL ?= "goldvip_uas.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_UPDATE_AGENTS_TARBALL} \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
