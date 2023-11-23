SUMMARY = "Gold VIP (Vehicle Integration Platform) XEN configurations"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=fafa1a4becd2f1c0df63f867ef81796a"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "${D}/etc/xen/auto"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += "bash"

do_install() {
	install -d ${DESTDIR}
	install -m 644 ${S}/xen/cfg/${MACHINE}/V2Xdomu.cfg ${DESTDIR}
}

FILES:${PN} += "/etc/xen/auto/V2Xdomu.cfg"
