# Copyright 2023 NXP

# The HSE Firmware is loaded through the real-time bootloader.
RDEPENDS:${PN}:remove = "hse-firmware"

CFLAGS:append = " -shared -fPIC -Wall -fno-builtin"

