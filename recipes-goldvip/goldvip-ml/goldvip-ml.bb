SUMMARY = "GoldVIP (Vehicle Integration Platform) Machine Learning demos"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_ML_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_ML_TARBALL ?= "eiqa_ml_apps.tgz"

SRC_URI = " \
    ${@f"""file://{d.getVar('GOLDVIP_ML_DIR')}/{d.getVar('GOLDVIP_ML_TARBALL')}""" \
        if os.path.exists(f"""{d.getVar('GOLDVIP_ML_DIR')}/{d.getVar('GOLDVIP_ML_TARBALL')}""") else ''} \
"

DEPENDS += " update-rc.d-native"
S = "${WORKDIR}"

DESTDIR = "/root/ml"

do_install() {
    install -d ${D}${DESTDIR}
    cp -R ${S}/pred_maintain ${D}${DESTDIR}
    cp -R ${S}/bms ${D}${DESTDIR}
    cp -R ${S}/speech_classification ${D}${DESTDIR}

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/service/* ${D}${sysconfdir}/init.d

    install -d ${D}/usr/lib
    install -m 0755 ${S}/libonnxruntime.so.1.8.1 ${D}/usr/lib/

    update-rc.d -r ${D} eiqa_pd defaults 80
    update-rc.d -r ${D} eiqa_bms defaults 80
    update-rc.d -r ${D} eiqa_sc defaults 80
}

FILES:${PN} += " \
    ${sysconfdir}/init.d/ \
    /usr/lib/libonnxruntime.so.1.8.1 \
    ${DESTDIR} \
"

