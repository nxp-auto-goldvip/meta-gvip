# Copyright 2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://mmcblk0.ignorelist \
"

do_install() {
    install -d ${D}${sysconfdir}/udev/mount.ignorelist.d
    install -m 0644 ${WORKDIR}/mmcblk0.ignorelist ${D}${sysconfdir}/udev/mount.ignorelist.d
}

CONFFILES_${PN} += "${sysconfdir}/udev/mount.ignorelist.d/mmcblk0.ignorelist"

