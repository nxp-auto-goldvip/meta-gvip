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

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += " \
    python3-flask \
    javascript-plotly \
"

do_install() {
    install -d ${DESTDIR}/telemetry-server
    install -d ${DESTDIR}/telemetry-server/templates
    install -m 0755 ${S}/telemetry-server/*.py ${DESTDIR}/telemetry-server
    install -m 0755 ${S}/cloud-gw/aws-lambda-functions/telemetry-function/domU/remote_client.py ${DESTDIR}/telemetry-server
    install -m 0755 ${S}/cloud-gw/aws-lambda-functions/telemetry-function/domU/server_config ${DESTDIR}/telemetry-server
    install -m 0755 ${S}/telemetry-server/templates/index.html ${DESTDIR}/telemetry-server/templates

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/telemetry-server/service/telemetry-server ${D}${sysconfdir}/init.d/telemetry-server
}

# set update-rc.d parameters
INITSCRIPT_NAME = "telemetry-server"
INITSCRIPT_PARAMS = "defaults 70"

FILES_${PN} += " \
    /home/root/telemetry-server/ \
    /etc/init.d/telemetry-server \
"