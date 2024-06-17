# Copyright 2023-2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = " \
    file://0001-Remove-ipc-shm-xen-module.patch \
"

BRANCH = "release/SW32G_IPCF_4.10.0_D2405"
SRCREV = "2419b87af860faac9f46a63536b0bbf906b31053"

# Create DEV PKG, in order to be used in modules which depend
# on the current package
do_install:append() {
    install -d ${D}${includedir}/${PN}
    install -m 0644 ${S}/ipc-shm.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/ipc-types.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/${PN}/
}

FILES:${PN}-dev += "${includedir}/${PN}/*"
