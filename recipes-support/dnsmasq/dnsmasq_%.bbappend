FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://dnsmasq"

DESTDIR = "${D}/${sysconfdir}/default/"

do_install_append () {
    install -d ${DESTDIR}
    install -m 0755 ${WORKDIR}/dnsmasq ${DESTDIR}/dnsmasq
}

FILES_${PN} += "${sysconfdir}/default/"