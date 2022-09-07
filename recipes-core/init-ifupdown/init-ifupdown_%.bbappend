FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Patch the default interfaces file depending on the content of DISTRO_FEATURES.
SRC_URI += "\
    file://0001-fix-ifconfig-path.patch;patch=1 \
    file://0002-add-aux0-interface.patch;patch=1 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'file://0001-add-xen-specific-interfaces.patch;patch=1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://0002-add-dummy0-interface.patch;patch=1', '', d)} \
"

