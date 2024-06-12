SUMMARY = "GoldVIP (Vehicle Integration Platform) Machine Learning demos"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_ML_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_ML_TARBALL ?= "eiqa_ml_apps.tgz"

SRC_URI = " \
    file://${GOLDVIP_ML_DIR}/${GOLDVIP_ML_TARBALL} \
"

DEPENDS += " update-rc.d-native"
S = "${WORKDIR}"

DESTDIR = "/home/root/ml"

do_install() {
    install -d ${D}${DESTDIR}
    cp -R ${S}/pred_maintain ${D}${DESTDIR}
    cp -R ${S}/bms ${D}${DESTDIR}

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/service/* ${D}${sysconfdir}/init.d

    install -d ${D}/usr/lib
    install -m 0755 ${S}/libonnxruntime.so.1.8.1 ${D}/usr/lib/

    update-rc.d -r ${D} eiqa_pd defaults 80
    update-rc.d -r ${D} eiqa_bms defaults 80
}

FILES:${PN} += " \
    ${sysconfdir}/init.d/ \
    /usr/lib/libonnxruntime.so.1.8.1 \
    ${DESTDIR} \
"

