SUMMARY = "Gold VIP (Vehicle Integration Platform) Remote Update Agent demo scripts"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=173a22a8dfc7298a74c300ecbfd30c6c"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_REMOTE_UPDATE_AGENTS_TARBALL  ?= "goldvip_remote_ua.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_REMOTE_UPDATE_AGENTS_TARBALL} \
"

S = "${WORKDIR}/remote_ua"
RDEPENDS_${PN} += " bash "
DESTDIR = "/home/root/ota/demo"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${DESTDIR}
    install -m 0755 ${S}/demo/* ${D}${DESTDIR}
}

FILES_${PN} += " \
    ${DESTDIR} \
"

