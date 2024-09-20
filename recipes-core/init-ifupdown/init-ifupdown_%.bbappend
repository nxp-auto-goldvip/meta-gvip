FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "\
    file://eth0-dhcp.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', \
        'file://eth0-manual.cfg file://xenbr0.cfg file://v2xbr.cfg file://eth1.cfg ', \
        'file://dummy1-2.cfg', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://dummy0.cfg', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://pfe-slave.cfg', 'file://pfe.cfg', d)} \
"

do_install:append () {
    install -d ${D}${sysconfdir}/network/interfaces.d
    install -m 0644 ${S}/eth0-dhcp.cfg ${D}${sysconfdir}/network/interfaces.d

    if ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'true', 'false', d)}; then
        install -m 0644 ${S}/dummy0.cfg ${D}${sysconfdir}/network/interfaces.d
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'true', 'false', d)}; then
        install -m 0644 ${S}/eth0-manual.cfg ${D}${sysconfdir}/network/interfaces.d
        install -m 0644 ${S}/xenbr0.cfg ${D}${sysconfdir}/network/interfaces.d
        install -m 0644 ${S}/v2xbr.cfg ${D}${sysconfdir}/network/interfaces.d
        install -m 0644 ${S}/eth1.cfg ${D}${sysconfdir}/network/interfaces.d
    else
        install -m 0644 ${S}/dummy1-2.cfg ${D}${sysconfdir}/network/interfaces.d
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'true', 'false', d)}; then
        install -m 0644 ${S}/pfe-slave.cfg ${D}${sysconfdir}/network/interfaces.d
    else
        install -m 0644 ${S}/pfe.cfg ${D}${sysconfdir}/network/interfaces.d
    fi
}

PACKAGES =+ "${PN}-linux ${PN}-dom0 ${PN}-domu ${PN}-pfe ${PN}-eth0-dhcp"

RDEPENDS:${PN}-linux += " ${PN}-eth0-dhcp ${PN}-pfe"
RDEPENDS:${PN}-dom0 += " ${PN}-pfe"
RDEPENDS:${PN}-domu += " ${PN}-eth0-dhcp"

FILES:${PN} += "\
    ${sysconfdir}/network/interfaces.d/dummy0.cfg \
"

FILES:${PN}-eth0-dhcp += "\
    ${sysconfdir}/network/interfaces.d/eth0-dhcp.cfg \
"

FILES:${PN}-pfe += "\
    ${sysconfdir}/network/interfaces.d/pfe*.cfg \
"

FILES:${PN}-linux += "\
    ${sysconfdir}/network/interfaces.d/dummy1-2.cfg \
"

FILES:${PN}-dom0 += "\
    ${sysconfdir}/network/interfaces.d/eth0-manual.cfg \
    ${sysconfdir}/network/interfaces.d/xenbr0.cfg \
    ${sysconfdir}/network/interfaces.d/v2xbr.cfg \
"

FILES:${PN}-domu += "\
    ${sysconfdir}/network/interfaces.d/eth1.cfg \
"
