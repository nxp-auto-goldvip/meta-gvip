FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
        file://0001-align-fip-to-64B.patch;patch=1 \
        file://0001-change-BL2-SRAM-memory-ranges.patch;patch=1 \        
        file://0001-remove-spi1-pins.patch;patch=1 \
"

