SUMMARY = "Gold VIP (Vehicle Integration Platform) CAN interfaces setup service"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=978b04a44d7a38ae169a251ba7cb82d1"

inherit update-rc.d

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

GOLDVIP_BINARIES_DIR ?= "."

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += " \
    bash \
"

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0755 ${S}/can-gw/config/can_config ${D}${sysconfdir}/can_config
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/can-gw/service/can ${D}${sysconfdir}/init.d/can
}

# set update-rc.d parameters
INITSCRIPT_NAME = "can"
INITSCRIPT_PARAMS = "defaults 90"

FILES_${PN} += "${sysconfdir}/can_config"
FILES_${PN} += "${sysconfdir}/init.d/can"
