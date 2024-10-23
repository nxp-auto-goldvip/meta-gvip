SUMMARY = "Gold VIP (Vehicle Integration Platform) Remote Update Agent"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

inherit update-rc.d
inherit features_check

REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

RDEPENDS:${PN} += " \
    bash \
    busybox \
    libcrypto \
    libssl \
"

DESTDIR = "/home/root/ota"

do_install() {
    install -d ${D}${DESTDIR}
    install -m 0755 ${ARTIFACTS_DIR}/goldvip_remote_ua ${D}${DESTDIR}/
    install -m 0755 ${ARTIFACTS_DIR}/*.sh ${D}${DESTDIR}/
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${ARTIFACTS_DIR}/service/goldvip-remote-ua ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/ota/remote_ua
    install -m 0755 ${ARTIFACTS_DIR}/conf/* ${D}${sysconfdir}/ota/remote_ua

    # Copy Uptane initial data.
    install -d ${D}/data
    cp -R ${ARTIFACTS_DIR}/abqdata/* ${D}/data/

    for ua_defaults in ${D}/data/*_ua/factory_defaults; do
        cp -R ${ua_defaults}/* $(dirname ${ua_defaults})/.
    done
}

# set update-rc.d parameters
INITSCRIPT_NAME = "goldvip-remote-ua"
INITSCRIPT_PARAMS = "defaults 99"

FILES:${PN} += " \
    ${DESTDIR} \
    ${sysconfdir}/init.d/goldvip-remote-ua \
    ${sysconfdir}/ota/remote_ua \
    /data \
"
