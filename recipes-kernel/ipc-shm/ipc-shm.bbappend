# Copyright 2023 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/patches:"

BRANCH = "release/SW32G_IPCF_4.9.0_D2310"
SRCREV = "e90d0a18eb6d53f1faa09739f3d1c00f6f4eed8c"

# Create DEV PKG, in order to be used in modules which depend
# on the current package
do_install:append() {
    install -d ${D}${includedir}/${PN}
    install -m 0644 ${S}/ipc-shm.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/${PN}/
}

FILES:${PN}-dev += "${includedir}/${PN}/*"

SRC_URI:append = " \
    file://0001-softirq-high-core-load.patch \
"
