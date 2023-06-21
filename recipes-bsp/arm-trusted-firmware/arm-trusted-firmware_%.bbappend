FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
        file://0001-change-BL2-SRAM-memory-ranges.patch;patch=1 \
"

EXTRA_OEMAKE += " \
        FIP_ALIGN=64 \
"
