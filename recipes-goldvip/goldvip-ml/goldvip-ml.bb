SUMMARY = "Gold VIP (Vehicle Integration Platform) Machine Learning demo"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

inherit update-rc.d

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_ML_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_ML_TARBALL ?= "pd_apps.tgz"

SRC_URI = " \
    file://${GOLDVIP_ML_DIR}/${GOLDVIP_ML_TARBALL} \
"

S = "${WORKDIR}"

DESTDIR = "${D}/home/root/ml"

do_install() {
    install -d ${DESTDIR}
    install -m 0644 ${S}/*.h ${DESTDIR}
    install -m 0644 ${S}/*.onnx ${DESTDIR}
    install -m 0644 ${S}/*.json ${DESTDIR}
    install -m 0644 ${S}/*.bin ${DESTDIR}

    install -d ${D}/usr/lib
    install -m 0755 ${S}/libonnxruntime.so.1.8.1 ${D}/usr/lib/

    install -d ${D}/usr/bin
    install -m 0755 ${S}/eiqa_pd ${D}/usr/bin/

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/eiqa_pd_service ${D}${sysconfdir}/init.d/eiqa_pd
}

# set update-rc.d parameters
INITSCRIPT_NAME = "eiqa_pd"
INITSCRIPT_PARAMS = "defaults 80"

FILES_${PN} += "/home/root/ml/"
FILES_${PN} += "/usr/lib/libonnxruntime.so.1.8.1"
FILES_${PN} += "/etc/init.d/eiqa_pd_service"
FILES_${PN} += "/usr/bin/eiqa_pd"
