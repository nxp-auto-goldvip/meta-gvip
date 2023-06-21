require nxp89xx-fw.inc

PACKAGES =+ "${PN}-rtlwifi"

FILES:${PN}-rtlwifi = " \
    ${nonarch_base_libdir}/firmware/rtlwifi/* \
"