# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append= " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://0001-fdts-s32g2-rdb2-slave-Add-RDB2-PFE-Slave-config.patch', '', d)} \
"

EXTRA_OEMAKE:append = " \
        FIP_ALIGN=64 \
        BL2_BASE=0x34610000 \
        BL2_LIMIT=0x346FFFFF \
"

