# Copyright 2023 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://0001-Invoke-read-write-with-memory-barries.patch \
"

BRANCH = "release/SW32G_IPCF_4.8.0_D2212"
SRCREV = "07321f760bc248f98a1c9ef27ef068ab73640e4b"

# Create DEV PKG, in order to be used in modules which depend
# on the current package
do_install:append() {
    install -d ${D}${includedir}/${PN}
    install -m 0644 ${S}/ipc-shm.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/${PN}/
}

FILES:${PN}-dev += "${includedir}/${PN}/*"

