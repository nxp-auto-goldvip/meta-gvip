SUMMARY = "Gold VIP (Vehicle Integration Platform) OTAmatic Client"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_OTAMATIC_TARBALL} \
"

# tell yocto not to extract the tarballs
INHIBIT_PACKAGE_STRIP = "1"

DESTDIR = "${D}/home/root/ota"
RDEPENDS_${PN} += " \
    bash \
    libcrypto \
    libssl \
    python3-rich \
    python3-websockets \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${DESTDIR}
    tar --no-same-owner -xzpf ${GOLDVIP_OTA_DIR}/${GOLDVIP_OTAMATIC_TARBALL} -C ${DESTDIR}
}

pkg_postinst_ontarget_${PN}() {
    cd /home/root/ota/output && ./reinstall.sh
}

FILES_${PN} += "/home/root/ota/"
