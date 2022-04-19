FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
# Dinamically patch interfaces if XEN is enabled
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'file://0001-add-goldvip-interfaces.patch;patch=1', 'file://0001-add-aux-interface.patch;patch=1', d)}"
