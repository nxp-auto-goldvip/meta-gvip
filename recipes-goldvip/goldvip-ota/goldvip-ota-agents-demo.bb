SUMMARY = "Gold VIP (Vehicle Integration Platform) Update Agents demo scripts"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_UPDATE_AGENTS_TARBALL ?= "goldvip_uas.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_UPDATE_AGENTS_TARBALL} \
"

S = "${WORKDIR}/goldvip_uas"
RDEPENDS:${PN} += " \
    bash \
    mtd-utils \
"
DESTDIR = "/home/root/ota/demo/"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${DESTDIR}
    install -m 0755 ${S}/demo/* ${D}${DESTDIR}
}

FILES:${PN} += " \
    ${DESTDIR} \
"

