# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Add the required kernel configs for GoldVIP
DELTA_KERNEL_DEFCONFIG:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'containerization.cfg', '', d)} \
    goldvip.cfg \
"

SRC_URI:append = " \
    file://patches/0001-arm64-dts-s32g-Disable-hif0sl-node-${PV}.patch \
    file://patches/0001-arm64-dts-s32g-Add-GoldVIP-specific-specs-${PV}.patch \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://build/containerization.cfg', '', d)} \
    file://build/goldvip.cfg \
"

