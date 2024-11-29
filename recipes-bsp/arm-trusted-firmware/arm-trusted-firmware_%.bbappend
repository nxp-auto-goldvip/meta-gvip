# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

DISABLE_SERDES1_FLAG = "false"
python() {
    if (oe.utils.vartrue('GOLDVIP_SKIP_SERDES1_CONFIG', True, False, d) and
        bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', True, False, d)):
        d.setVar('DISABLE_SERDES1_FLAG', "true")
}

SRC_URI:append= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://0001-fdts-s32g2-rdb2-slave-Add-RDB2-PFE-Slave-config.patch', '', d)} \
    ${@oe.utils.vartrue('DISABLE_SERDES1_FLAG', 'file://0002-fdts-s32g-Disable-serdes1-node-in-PFE-Slave-DT.patch', '', d)} \
"

EXTRA_OEMAKE:append = " \
    FIP_ALIGN=64 \
    BL2_BASE=0x34610000 \
    BL2_LIMIT=0x346FFFFF \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'ERRATA_ERR052269=0', '', d)} \
"

