# Copyright 2023 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append= " \
        file://0001-s32-Update-BL2_LIMIT-for-GoldVIP-usage.patch \
        ${@bb.utils.contains('DISTRO_FEATURES', 'scprt pfe-slave', 'file://0001-fdts-s32g-rdb3-Add-dummy-PFE-slave-config.patch', '', d)} \
"

SCPRT_DTB_FILE_NAME = "${@os.path.basename(d.getVar('KERNEL_DEVICETREE'))}"

EXTRA_OEMAKE:append = " \
        FIP_ALIGN=64 \
        BL2_BASE=0x34610000 \
"

EXTRA_OEMAKE:append:s32g3 = " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'scprt pfe-slave', 'DTB_FILE_NAME=${SCPRT_DTB_FILE_NAME}', '', d)} \
"

