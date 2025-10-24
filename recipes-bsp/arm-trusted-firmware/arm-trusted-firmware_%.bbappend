# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://0002-fdts-s32g-Add-GoldVIP-specs.patch', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'secboot', 'file://0001-fdts-s32g-Use-HSE-MU1-for-ATF-Crypto.patch', '', d)} \
"

EXTRA_OEMAKE:append = " \
    FIP_ALIGN=64 \
    BL2_BASE=0x34610000 \
    BL2_LIMIT=0x346FFFFF \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'ERRATA_ERR052269=0', '', d)} \
"