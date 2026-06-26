SUMMARY = "Gold VIP (Vehicle Integration Platform) Motion Wise Communication application"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

MOTION_WISE_APP_DIR ?= "${ARTIFACTS_DIR}/mpu-app"

RDEPENDS:${PN} += " \
    goldvip-motion-wise \
    bash \
"

SKIP_FILEDEPS:${PN} = "1"
INSANE_SKIP:${PN} += "already-stripped ldflags file-rdeps"

do_install() {
    # Install application
    install -d ${D}/root/motion-wise
    install -m 0755 ${MOTION_WISE_APP_DIR}/motion-wise-mpu-app ${D}/root/motion-wise/
    install -m 0755 ${MOTION_WISE_APP_DIR}/run_dds_subscriber.sh ${D}/root/motion-wise/
    install -m 0644 ${MOTION_WISE_APP_DIR}/config_aux0sl.xml ${D}/root/motion-wise/
}

FILES:${PN} += "/root/motion-wise"