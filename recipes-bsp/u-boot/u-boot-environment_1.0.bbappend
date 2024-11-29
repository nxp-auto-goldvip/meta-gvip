# Copyright 2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}/goldvip-bootconfig-${@oe.utils.conditional('GOLDVIP_DYNAMIC_BOOTCONFIG', 'true', 'on', 'off', d)}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}/goldvip-bootloader-${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', 'on', 'off', d)}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}/goldvip-gateway-app-${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'on', 'off', d)}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}/goldvip:"
