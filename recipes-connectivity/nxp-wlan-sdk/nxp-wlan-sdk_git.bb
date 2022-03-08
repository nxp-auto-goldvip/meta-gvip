# Provides the mlan kernel module and the nxp wlan sdk

require nxp-wlan-sdk_git.inc

SUMMARY = "NXP Wi-Fi SDK"

inherit module-base

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile () {
    oe_runmake build
}

do_install () {
    install -d ${D}${datadir}/nxp_wireless
    install -m 0755 mapp/mlanutl/mlanutl ${D}${datadir}/nxp_wireless
    install -m 0755 script/load ${D}${datadir}/nxp_wireless
    install -m 0755 script/unload ${D}${datadir}/nxp_wireless
    install -m 0644 README_MLAN ${D}${datadir}/nxp_wireless
}

FILES_${PN} = "${datadir}/nxp_wireless"

COMPATIBLE_MACHINE = "s32g"
