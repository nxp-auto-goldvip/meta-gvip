require nxp89xx-fw.inc

PACKAGES =+ "${PN}-rtlwifi"

FILES_${PN}-rtlwifi = " \
    ${nonarch_base_libdir}/firmware/rtlwifi/* \
"