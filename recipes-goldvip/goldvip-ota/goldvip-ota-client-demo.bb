SUMMARY = "Gold VIP (Vehicle Integration Platform) OTAmatic Client demo scripts"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    ${@f"""file://{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_OTAMATIC_TARBALL')}""" \
        if os.path.exists(f"""{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_OTAMATIC_TARBALL')}""") else ''} \
"

S = "${WORKDIR}/otamatic/resources"
DESTDIR = "/root/ota/demo"

RDEPENDS:${PN} += " \
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

FILES:${PN} += " \
    ${DESTDIR} \
"
