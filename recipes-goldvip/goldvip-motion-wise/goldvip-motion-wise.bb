ARTIFACTS_DIR = "${WORKDIR}"

require recipes-goldvip/goldvip-motion-wise/goldvip-motion-wise.inc

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_MOTION_WISE_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_MOTION_WISE_TARBALL ?= "motion_wise_com.tgz"

SRC_URI = " \
    ${@f"""file://{d.getVar('GOLDVIP_MOTION_WISE_DIR')}/{d.getVar('GOLDVIP_MOTION_WISE_TARBALL')}""" \
        if os.path.exists(f"""{d.getVar('GOLDVIP_MOTION_WISE_DIR')}/{d.getVar('GOLDVIP_MOTION_WISE_TARBALL')}""") else ''} \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
