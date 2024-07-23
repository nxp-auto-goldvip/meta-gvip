ARTIFACTS_DIR = "${WORKDIR}/remote_ua"

require recipes-goldvip/goldvip-ota/goldvip-remote-ua.inc


GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_REMOTE_UPDATE_AGENT_TARBALL ?= "goldvip_remote_ua.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_REMOTE_UPDATE_AGENT_TARBALL} \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
