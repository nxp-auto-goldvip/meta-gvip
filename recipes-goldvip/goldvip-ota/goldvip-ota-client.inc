SUMMARY = "Gold VIP (Vehicle Integration Platform) OTAmatic Client"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit update-rc.d
inherit features_check

REQUIRED_DISTRO_FEATURES ?= " goldvip-ota"

RDEPENDS:${PN} += " \
    bash \
    libcrypto \
    libssl \
"

do_install() {
    # Service running in non-containerized environment.
    install -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${ARTIFACTS_DIR}/otamatic/resources/service/otamatic ${D}/${sysconfdir}/init.d/

    install -d ${D}/${bindir}
    install -m 0755 ${ARTIFACTS_DIR}/otamatic/otamatic_sample_app ${D}/${bindir}/

    # Copy Uptane initial data.
    install -d ${D}/data
    cp -R ${ARTIFACTS_DIR}/otamatic/resources/abqdata/* ${D}/data/
    cp -R ${D}/data/otamatic/factory_default_uptane ${D}/data/otamatic/uptane
}

# Set update-rc.d parameters.
INITSCRIPT_NAME = "otamatic"
INITSCRIPT_PARAMS = "defaults 98"

FILES:${PN} += " \
    /etc/init.d/otamatic \
    ${bindir}/otamatic_sample_app \
    /data \
"