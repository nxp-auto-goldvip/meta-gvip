# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/patches:"

# Add the required kernel configs for GoldVIP
DELTA_KERNEL_DEFCONFIG:append = " \
    fleetwise.cfg \
    greengrass.cfg \
    goldvip.cfg \
    ipsec.cfg \
    usb_network.cfg \
    pcie.cfg \
"

SRC_URI:append = " \
    file://build/fleetwise.cfg \
    file://build/greengrass.cfg \
    file://build/goldvip.cfg \
    file://build/ipsec.cfg \
    file://build/usb_network.cfg \
    file://build/pcie.cfg \
    file://0001-goldvip-dts-adaptations-${PV}.patch \
    file://0001-s32g-dts-Disable-spi1-node-${PV}.patch \
    file://0001-disable-stm7-and-can-ts-ctrl-${PV}.patch \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe', 'file://0001-enable-pfe-aux-netif-${PV}.patch', '', d)} \
"

# Containerization configuration
DELTA_KERNEL_DEFCONFIG:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'containerization.cfg', '', d)} \
"
SRC_URI:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://build/containerization.cfg', '', d)} \
"

