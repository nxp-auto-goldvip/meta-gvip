SUMMARY = "Gold VIP (Vehicle Integration Platform)"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit update-rc.d 

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

GOLDVIP_BINARIES_DIR ?= "."

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/cloud-gw/aws-lambda-functions/telemetry-function"
DESTDIR = "${D}/home/root/cloud-gw/telemetry-collector"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += " \
    bash \
    goldvip-telemetry-packages \
    python3-mmap \
    python3-fcntl \
    python3-rticonnextdds-connector \
    python3-pysensors \
"

do_install() {
    install -d ${DESTDIR}
    install -m 0755 ${S}/dom0/*.py ${DESTDIR}
    install -m 0755 ${S}/dom0/local_server_config ${DESTDIR}
    install -m 0666 ${S}/dom0/dds_telemetry.xml ${DESTDIR}
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/dom0/service/telemetry ${D}${sysconfdir}/init.d/telemetry
}

# set update-rc.d parameters
INITSCRIPT_NAME = "telemetry"
INITSCRIPT_PARAMS = "defaults 70"

FILES:${PN} += "/home/root/cloud-gw/telemetry-collector/*"
FILES:${PN} += "/etc/init.d/telemetry"
