ARTIFACTS_DIR = "${WORKDIR}"

require recipes-goldvip/goldvip-ota/goldvip-ota-client.inc

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_OTAMATIC_TARBALL} \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"