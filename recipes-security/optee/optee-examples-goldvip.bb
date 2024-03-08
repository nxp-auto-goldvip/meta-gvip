# Copyright 2024 NXP

require recipes-security/optee/optee-nxp.inc
require recipes-security/optee/optee-examples.inc

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"
SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/op-tee"

do_install () {
    mkdir -p ${D}${bindir}
    mkdir -p ${D}${libdir}
    install -D -p -m0755 ${B}/ca/optee_certificate_secure_storage ${D}${bindir}
    install -D -p -m0755 ${B}/ca/optee_certificate_secure_storage.so ${D}${libdir}/optee_certificate_secure_storage.so
}

FILES:${PN} += " ${libdir}/optee_certificate_secure_storage.so"
