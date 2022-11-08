FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
    file://pkcs11-hse.module \
    file://pkcs11-hse-remote.module \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/pkcs11-hse*.module ${D}${datadir}/p11-kit/modules
}

PACKAGES =+ "${PN}-hse ${PN}-hse-remote"
RDEPENDS_${PN}-hse = "${PN}"
RDEPENDS_${PN}-hse-remote = "${PN}"

FILES_${PN}-hse = "${datadir}/${PN}/modules/pkcs11-hse.module"
FILES_${PN}-hse-remote = "${datadir}/${PN}/modules/pkcs11-hse-remote.module"

BBCLASSEXTEND = "native nativesdk"
