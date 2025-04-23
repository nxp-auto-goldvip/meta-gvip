SUMMARY = "Gold VIP (Vehicle Integration Platform) Update Agents demo scripts"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_UPDATE_AGENTS_TARBALL ?= "goldvip_uas.tgz"

SRC_URI = " \
    ${@f"""file://{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_UPDATE_AGENTS_TARBALL')}""" \
        if os.path.exists(f"""{d.getVar('GOLDVIP_OTA_DIR')}/{d.getVar('GOLDVIP_UPDATE_AGENTS_TARBALL')}""") else ''} \
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
