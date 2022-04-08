FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " file://0001-add-goldvip-env-settings.patch;patch=1 \
             file://0002-remove-msi-parent-for-pci-in-xen.patch;patch=1 \
             "
