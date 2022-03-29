# Create DEV PKG, in order to be used in modules which depend
# on the current package
do_install_append() {
    install -d ${D}${includedir}/${PN}
    install -m 0644 ${S}/ipc-shm.h ${D}${includedir}/${PN}/
    install -m 0644 ${S}/Module.symvers ${D}${includedir}/${PN}/
}

FILES_${PN}-dev += "${includedir}/${PN}/*"

