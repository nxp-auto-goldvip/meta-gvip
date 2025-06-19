SUMMARY = "Gold VIP (Vehicle Integration Platform) RTI Connext Micro DDS Demo"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-dds goldvip-gateway"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/dds"
DESTDIR = "${D}/root"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += " \
    bash \
    goldvip-can-setup \
    python3-rticonnextdds-connector \
"

do_install() {
    install -d ${DESTDIR}/dds
    install -m 0755 ${S}/rti_dds_lights.py ${DESTDIR}/dds
    install -m 0666 ${S}/rti_dds_lights.xml ${DESTDIR}/dds
    install -m 0666 ${S}/rti_dds_lights.pcap ${DESTDIR}/dds
}

FILES:${PN} += "/root/dds/*"
