SUMMARY = "Gateway VIP (Vehicle Integration Platform)"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

GVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/gvip/gvip;protocol=https"
GVIP_BRANCH ?= "develop"

SRC_URI = "${GVIP_URL};branch=${GVIP_BRANCH}"
SRCREV = "5969e18f5d02ba3ceba99fd704a4fa34d9e0eee3"

S = "${WORKDIR}/git"
DESTDIR="${D}/home/root"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += "bash"

do_install() {
	install -d ${DESTDIR}/eth-gw
	install -m 0755 ${S}/eth-gw/*-target.sh ${DESTDIR}/eth-gw
	install -d ${DESTDIR}/can-gw
	install -m 0755 ${S}/can-gw/*.sh ${DESTDIR}/can-gw
}

FILES_${PN} += "/home/root/can-gw/*"
FILES_${PN} += "/home/root/eth-gw/*"
