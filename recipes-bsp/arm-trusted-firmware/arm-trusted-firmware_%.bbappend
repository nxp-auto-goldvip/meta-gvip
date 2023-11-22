FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
        file://0001-s32-Update-BL2_LIMIT-for-GoldVIP-usage.patch \
"

EXTRA_OEMAKE += " \
        FIP_ALIGN=64 \
        BL2_BASE=0x34610000 \
"
