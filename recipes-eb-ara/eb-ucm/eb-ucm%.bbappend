# Copyright 2024 NXP

do_install:append() {
    install -m 0666 ${B}/impl/ucm_daemon/ara_UCM_mm.json ${D}${sysconfdir}/adaptive/ara_UCM

    install -d ${D}/containers/A ${D}/containers/B
    ln -s /containers/A ${D}/containers/target
}

FILES:${PN} += "/containers*"