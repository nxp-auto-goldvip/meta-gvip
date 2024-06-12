SUMMARY = "Gold VIP (Vehicle Integration Platform)"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

GOLDVIP_BINARIES_DIR ?= "."

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "${D}/home/root"
LOCAL_SBINDIR = "${D}/usr/local/sbin"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += " \
    bash \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', 'goldvip-can-setup', '', d)} \
    goldvip-telemetry-packages \
    python3-mmap \
    python3-fcntl \
"

DEPENDS:append = " update-rc.d-native"

do_install() {
    install -d ${DESTDIR}/eth-gw
    install -m 0755 ${S}/eth-gw/*.sh ${DESTDIR}/eth-gw
    install -d ${D}/${sysconfdir}
    install -m 0644 ${GOLDVIP_BINARIES_DIR}/idps.conf ${D}/${sysconfdir}
    install -d ${LOCAL_SBINDIR}
    install -m 0755 ${GOLDVIP_BINARIES_DIR}/linux_someip_idps ${LOCAL_SBINDIR}

    install -d ${DESTDIR}/can-gw
    install -m 0755 ${S}/can-gw/*.sh ${DESTDIR}/can-gw
    install -m 0755 ${S}/can-gw/*.py ${DESTDIR}/can-gw

    install -d ${D}/${sbindir}
    install -m 0755 ${S}/common/m7_core_load.py ${D}/${sbindir}

    install -d ${D}${sysconfdir}/init.d

    install -m 0755 ${S}/can-gw/service/avtp_listener ${D}${sysconfdir}/init.d/avtp_listener
    update-rc.d -r ${D} avtp_listener defaults 90

    install -m 0755 ${S}/eth-gw/service/wifi_service ${D}${sysconfdir}/init.d/wifi_service
    install -m 0644 ${S}/eth-gw/service/wifi_nxp.conf ${D}/${sysconfdir}
    update-rc.d -r ${D} wifi_service defaults 91

    install -d ${D}${exec_prefix}/bin
    install -m 0755 ${S}/eth-gw/setup_scripts/wifi_setup ${D}${exec_prefix}/bin/wifi_setup

    if ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'true', 'false', d)}; then
        install -m 0755 ${S}/eth-gw/service/pfe-slave-setup ${D}${sysconfdir}/init.d/pfe-slave-setup
        update-rc.d -r ${D} pfe-slave-setup defaults 85
    fi
}

FILES:${PN} += " \
    /home/root/can-gw/* \
    /home/root/eth-gw/* \
    ${sysconfdir}/* \
    /usr/local/sbin/* \
    ${sbindir}/*.py \
    ${sysconfdir}/init.d/avtp_listener \
    ${sysconfdir}/init.d/wifi_service \
    ${exec_prefix}/bin/wifi_service \
"
