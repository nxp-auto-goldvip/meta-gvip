ARTIFACTS_DIR = "${WORKDIR}/goldvip_uas"

require recipes-goldvip/goldvip-ota/goldvip-ota-agents.inc

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_UPDATE_AGENTS_TARBALL ?= "goldvip_uas.tgz"

SRC_URI = " \
    ${@f"""file://{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_UPDATE_AGENTS_TARBALL')}""" \
        if os.path.exists(f"""{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_UPDATE_AGENTS_TARBALL')}""") else ''} \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
