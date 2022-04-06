SUMMARY = "Gold VIP (Vehicle Integration Platform) OTAmatic Client"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

inherit update-rc.d
inherit features_check
REQUIRED_DISTRO_FEATURES ?= " goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_OTAMATIC_TARBALL} \
"

VEHIF_STATE_DIR="/home/root/ota/vehif_state"
S = "${WORKDIR}/otamatic"
RDEPENDS_${PN} += " \
    bash \
    libcrypto \
    libssl \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # Service running in non-containerized environment.
    install -d ${D}/${sysconfdir}/init.d
    install -m 0755 ${S}/resources/service/otamatic ${D}/${sysconfdir}/init.d/

    install -d ${D}/${bindir}
    install -m 0755 ${S}/otamatic_sample_app ${D}/${bindir}/

    # Copy Uptane initial data.
    install -d ${D}/data
    cp -R ${S}/resources/abqdata/* ${D}/data/
    cp -R ${D}/data/otamatic/factory_default_uptane ${D}/data/otamatic/uptane

    # Initialize the simulated vehicle state.
    install -d ${D}/${VEHIF_STATE_DIR}
    cp -R ${S}/resources/vehif_state/* ${D}/${VEHIF_STATE_DIR}
}

# Set update-rc.d parameters.
INITSCRIPT_NAME = "otamatic"
INITSCRIPT_PARAMS = "defaults 98"

FILES_${PN} += " \
    ${VEHIF_STATE_DIR} \
    /etc/init.d/otamatic \
    ${bindir}/otamatic_sample_app \
    /data \
"
