# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Add the required kernel configs for GoldVIP
DELTA_KERNEL_DEFCONFIG:append = " \
    fleetwise.cfg \
    greengrass.cfg \
    goldvip.cfg \
    ipsec.cfg \
    usb_network.cfg \
    pcie.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'containerization.cfg', '', d)} \
"

SRC_URI:append = " \
    file://patches/0001-arm64-dts-s32g-Add-GoldVIP-specific-specs.patch \
    file://build/fleetwise.cfg \
    file://build/greengrass.cfg \
    file://build/goldvip.cfg \
    file://build/ipsec.cfg \
    file://build/usb_network.cfg \
    file://build/pcie.cfg \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://build/containerization.cfg', '', d)} \
"

