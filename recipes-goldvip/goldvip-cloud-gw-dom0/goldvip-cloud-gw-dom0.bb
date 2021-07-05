SUMMARY = "Gold VIP (Vehicle Integration Platform)"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

inherit update-rc.d 

GOLDVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

GOLDVIP_BINARIES_DIR ?= "."

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/linux/cloud-gw/aws-lambda-functions/telemetry-function"
DESTDIR = "${D}/home/root/cloud-gw/telemetry-collector"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += "bash"

do_install() {
	install -d ${DESTDIR}
	install -m 0755 ${S}/dom0/*.py ${DESTDIR}
	install -m 0755 ${S}/dom0/config.json ${DESTDIR}
	install -m 0600 ${S}/domU/server_config ${DESTDIR}
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/dom0/service/telemetry ${D}${sysconfdir}/init.d/telemetry
}

# set update-rc.d parameters
INITSCRIPT_NAME = "telemetry"
INITSCRIPT_PARAMS = "defaults 70"

FILES_${PN} += "/home/root/cloud-gw/telemetry-collector/*"
FILES_${PN} += "/etc/init.d/telemetry"
