FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Patch the default interfaces file depending on the content of DISTRO_FEATURES.
SRC_URI += "\
    file://0001-add-aux0-interface.patch;patch=1 \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'file://0002-add-xen-specific-interfaces.patch;patch=1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'file://0002-add-dummy0-interface.patch;patch=1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'file://0002-enable-pfe-slave-interfaces.patch;patch=1', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'xen', '', 'file://0003-add-dummy-interfaces-nohv.patch;patch=1', d)} \
"

