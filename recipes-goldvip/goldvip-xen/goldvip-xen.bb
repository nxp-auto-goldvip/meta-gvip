SUMMARY = "Gold VIP (Vehicle Integration Platform) XEN configurations"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

GOLDVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "${D}/etc/xen/auto"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS_${PN} += "bash"

do_install() {
	install -d ${DESTDIR}
	install -m 644 ${S}/xen/cfg/V2Xdomu.cfg ${DESTDIR}
}

FILES_${PN} += "/etc/xen/auto/V2Xdomu.cfg"
