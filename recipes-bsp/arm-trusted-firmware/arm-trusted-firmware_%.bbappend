# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append= " \
        file://0001-s32-Update-BL2_LIMIT-for-GoldVIP-usage.patch \
        ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://0001-fdts-s32g2-rdb2-slave-Add-RDB2-PFE-Slave-config.patch', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'scprt', 'file://0001-fdts-s32g-Use-MSCM-1-interrupt-for-SCP-notifications.patch', '', d)} \
"

EXTRA_OEMAKE:append = " \
        FIP_ALIGN=64 \
        BL2_BASE=0x34610000 \
"

EXTRA_OEMAKE:append = " \
        DTB_FILE_NAME=${@os.path.basename(d.getVar('KERNEL_DEVICETREE'))} \
"

