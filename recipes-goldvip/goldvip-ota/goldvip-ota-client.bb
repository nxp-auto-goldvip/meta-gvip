SUMMARY = "Gold VIP (Vehicle Integration Platform) OTAmatic Client"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit update-rc.d
inherit features_check
REQUIRED_DISTRO_FEATURES ?= " goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_OTAMATIC_TARBALL ?= "otamatic.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_OTAMATIC_TARBALL} \
"

S = "${WORKDIR}/otamatic"
RDEPENDS:${PN} += " \
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
}

# Set update-rc.d parameters.
INITSCRIPT_NAME = "otamatic"
INITSCRIPT_PARAMS = "defaults 98"

FILES:${PN} += " \
    /etc/init.d/otamatic \
    ${bindir}/otamatic_sample_app \
    /data \
"
