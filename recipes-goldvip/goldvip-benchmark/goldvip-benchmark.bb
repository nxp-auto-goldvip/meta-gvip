SUMMARY = "Gold VIP (Vehicle Integration Platform) benchmark demo"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=978b04a44d7a38ae169a251ba7cb82d1"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "/home/root/benchmark"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += " \
    bash \
    coremark \
    kernel-module-ipc-chardev \
    python3 \
"

do_install() {
    install -d ${D}${DESTDIR}
    cp -r ${S}/benchmark/* ${D}${DESTDIR}
}

FILES:${PN} += "${DESTDIR}"

