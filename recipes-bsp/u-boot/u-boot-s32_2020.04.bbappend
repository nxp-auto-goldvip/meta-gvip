FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-configs-Add-GoldVIP-extra-commands.patch;patch=1 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://0002-configs-Updates-required-for-PFE-slave-instance.patch;patch=1', '', d)} \
"

