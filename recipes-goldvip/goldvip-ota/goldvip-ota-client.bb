ARTIFACTS_DIR = "${WORKDIR}"

require recipes-goldvip/goldvip-ota/goldvip-ota-client.inc

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    ${@f"""file://{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_OTAMATIC_TARBALL')}""" \
        if os.path.exists(f"""{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_OTAMATIC_TARBALL')}""") else ''} \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"