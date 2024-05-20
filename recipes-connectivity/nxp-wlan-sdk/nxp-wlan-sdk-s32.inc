# Copyright 2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/nxp-wlan-sdk:"

SRC_URI += "\
    file://0001-mxm_wifiex-Fix-build-errors-with-kernel-5.15.153-rt.patch;striplevel=3 \
"

RDEPENDS:${PN}:remove = "wireless-tools"
