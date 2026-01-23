FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://head"

DESTDIR = "${D}/${sysconfdir}/resolvconf/resolv.conf.d"

do_install:append () {
    install -d ${DESTDIR}
    install -m 0755 ${WORKDIR}/head ${DESTDIR}/head
}

FILES:${PN} += "${sysconfdir}/resolvconf/resolv.conf.d"