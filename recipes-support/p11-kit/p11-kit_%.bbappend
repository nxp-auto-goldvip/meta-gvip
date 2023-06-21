FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += " \
    file://pkcs11-hse.module \
    file://pkcs11-hse-remote.module \
"

do_install:append() {
    install -m 0644 ${WORKDIR}/pkcs11-hse*.module ${D}${datadir}/p11-kit/modules
}

PACKAGES =+ "${PN}-hse ${PN}-hse-remote"
RDEPENDS:${PN}-hse = "${PN}"
RDEPENDS:${PN}-hse-remote = "${PN}"

FILES:${PN}-hse = "${datadir}/${PN}/modules/pkcs11-hse.module"
FILES:${PN}-hse-remote = "${datadir}/${PN}/modules/pkcs11-hse-remote.module"

BBCLASSEXTEND = "native nativesdk"
