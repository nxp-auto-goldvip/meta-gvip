SUMMARY = "Gold VIP (Vehicle Integration Platform)"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

inherit update-rc.d 

GOLDVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

GOLDVIP_BINARIES_DIR ?= "."

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "${D}/home/root"
LOCAL_SBINDIR = "${D}/usr/local/sbin"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += "bash"

do_install() {
	install -d ${DESTDIR}/eth-gw
	install -m 0755 ${S}/eth-gw/*-target.sh ${DESTDIR}/eth-gw
	install -m 0755 ${S}/eth-gw/eth-common.sh ${DESTDIR}/eth-gw
	install -d ${D}/${sysconfdir}
	install -m 0644 ${GOLDVIP_BINARIES_DIR}/idps.conf ${D}/${sysconfdir}
	install -d ${LOCAL_SBINDIR}
	install -m 0755 ${GOLDVIP_BINARIES_DIR}/linux_someip_idps ${LOCAL_SBINDIR}

	install -d ${DESTDIR}/can-gw
	install -m 0755 ${S}/can-gw/*.sh ${DESTDIR}/can-gw
	install -m 0755 ${S}/can-gw/*.py ${DESTDIR}/can-gw
	install -m 0755 ${S}/cloud-gw/aws-lambda-functions/telemetry-function/dom0/m7_stats.py ${DESTDIR}/can-gw
	
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/can-gw/service/avtp_listener ${D}${sysconfdir}/init.d/avtp_listener
}

FILES_${PN} += "/home/root/can-gw/*"
FILES_${PN} += "/home/root/eth-gw/*"
FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "/usr/local/sbin/*"
FILES_${PN} += "${sysconfdir}/init.d/avtp_listener"

# set update-rc.d parameters
INITSCRIPT_NAME = "avtp_listener"
INITSCRIPT_PARAMS = "defaults 90"
