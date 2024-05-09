SUMMARY = "Gold VIP (Vehicle Integration Platform) XEN configurations"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "${D}/${sysconfdir}/xen"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += "bash"

do_install() {
	install -d ${DESTDIR}/auto
	install -d ${DESTDIR}/cfg
	install -m 644 ${S}/xen/cfg/${MACHINE}/V2Xdomu* ${DESTDIR}/cfg
	ln -s ${sysconfdir}/xen/cfg/V2Xdomu${@bb.utils.contains('DISTRO_FEATURES', 'optee', '_optee', '', d)}.cfg ${DESTDIR}/auto/V2Xdomu.cfg
}

FILES:${PN} += "/etc/xen/auto/V2Xdomu.cfg"
