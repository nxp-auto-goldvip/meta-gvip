# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Enable the PFE Slave driver if pfe-slave is in DISTRO_FEATURES
DELTA_UBOOT_DEFCONFIG:append:s32 = " ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', ' pfe_slave_config.cfg', '', d)}"
SRC_URI:append:s32 = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://build/pfe_slave_config.cfg', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'scprt', 'file://0001-drivers-misc-nvmem-scmi-Fix-READ_CELL-p2a-parameters.patch', '', d)} \
"

