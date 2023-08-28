SUMMARY = "Gold VIP (Vehicle Integration Platform) telemetry python package"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=eb2d80feeaeafaa489a164e349e12342"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/cloud-gw/goldvip-telemetry-packages"

inherit setuptools3
