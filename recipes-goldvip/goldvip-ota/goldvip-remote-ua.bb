SUMMARY = "Gold VIP (Vehicle Integration Platform) Remote Update Agent"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit update-rc.d
inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_REMOTE_UPDATE_AGENT_TARBALL ?= "goldvip_remote_ua.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_REMOTE_UPDATE_AGENT_TARBALL} \
"

S = "${WORKDIR}/remote_ua"
RDEPENDS:${PN} += " \
    bash \
    busybox \
    libcrypto \
    libssl \
"
DESTDIR = "/home/root/ota"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${DESTDIR}
    install -m 0755 ${S}/goldvip_remote_ua ${D}${DESTDIR}/
    install -m 0755 ${S}/*.sh ${D}${DESTDIR}/
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/service/goldvip-remote-ua ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/ota/remote_ua
    install -m 0755 ${S}/conf/* ${D}${sysconfdir}/ota/remote_ua

    # Copy Uptane initial data.
    install -d ${D}/data
    cp -R ${S}/abqdata/* ${D}/data/

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
