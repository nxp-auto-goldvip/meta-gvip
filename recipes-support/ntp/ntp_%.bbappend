FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://ntp.conf"

do_install_append() {
        install -m 0644 ${WORKDIR}/ntp.conf ${D}${sysconfdir}
}
