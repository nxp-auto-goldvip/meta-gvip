FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/patches:"

# Add the required kernel configs for GoldVIP
DELTA_KERNEL_DEFCONFIG_append = " \
    fleetwise.cfg \
    greengrass.cfg \
    goldvip.cfg \
    ipsec.cfg \
    usb_network.cfg \
    pcie.cfg \
"

SRC_URI_append = " \
    file://build/fleetwise.cfg \
    file://build/greengrass.cfg \
    file://build/goldvip.cfg \
    file://build/ipsec.cfg \
    file://build/usb_network.cfg \
    file://build/pcie.cfg \
    file://0001-enable-aux-interface-in-pfe.patch;patch=1 \
    file://0001-add-dts-reserved-memory-regions.patch;patch=1 \
"

# Containerization configuration
DELTA_KERNEL_DEFCONFIG_append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'containerization.cfg', '', d)} \
"
SRC_URI_append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://build/containerization.cfg', '', d)} \
"
