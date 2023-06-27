SUMMARY = "Gold VIP (Vehicle Integration Platform) OTAmatic Client demo scripts"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=978b04a44d7a38ae169a251ba7cb82d1"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_OTAMATIC_TARBALL} \
"

S = "${WORKDIR}/otamatic/resources"
DESTDIR = "/home/root/ota/demo"

RDEPENDS_${PN} += " \
    bash \
    python3-requests \
    python3-rich \
    python3-websockets \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${DESTDIR}
    cp -R ${S}/demo/* ${D}${DESTDIR}
}

FILES_${PN} += " \
    ${DESTDIR} \
"

