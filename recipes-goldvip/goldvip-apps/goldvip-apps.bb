SUMMARY = "Gold VIP (Vehicle Integration Platform)"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

GOLDVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/gvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/linux"
DESTDIR="${D}/home/root"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += "bash"

do_install() {
	install -d ${DESTDIR}/eth-gw
	install -m 0755 ${S}/eth-gw/*-target.sh ${DESTDIR}/eth-gw
	install -m 0755 ${S}/eth-gw/eth-common.sh ${DESTDIR}/eth-gw
	install -d ${DESTDIR}/can-gw
	install -m 0755 ${S}/can-gw/*.sh ${DESTDIR}/can-gw
	install -m 0755 ${S}/can-gw/*.py ${DESTDIR}/can-gw
	install -m 0755 ${S}/cloud-gw/aws-lambda-functions/telemetry-function/m7_stats.py ${DESTDIR}/can-gw
	install -d ${DESTDIR}/cloud-gw
	install -m 0755 ${S}/cloud-gw/greengrass_provision.py ${DESTDIR}/cloud-gw
}

FILES_${PN} += "/home/root/can-gw/*"
FILES_${PN} += "/home/root/cloud-gw/*"
FILES_${PN} += "/home/root/eth-gw/*"
