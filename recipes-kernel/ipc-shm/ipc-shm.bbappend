# Copyright 2023-2025 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

BRANCH = "release/SW32G_IPCF_4.11.0"
SRCREV = "92c3b834f3e175cf59782099d2b2df2b63177996"

EXTRA_OEMAKE += "IPC_USE_XEN=NO"

# Create DEV PKG, in order to be used in modules which depend
# on the current package
do_install:append() {
    install -d ${D}${includedir}/${PN}
    install -m 0644 ${S}/ipc-shm.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/ipc-types.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/${PN}/
}

FILES:${PN}-dev += "${includedir}/${PN}/*"
