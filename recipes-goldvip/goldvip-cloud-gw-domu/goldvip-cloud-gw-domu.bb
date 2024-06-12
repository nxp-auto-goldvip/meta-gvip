SUMMARY = "Gold VIP (Vehicle Integration Platform)"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit update-rc.d

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

GOLDVIP_BINARIES_DIR ?= "."

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
DESTDIR = "${D}/home/root"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RDEPENDS:${PN} += " \
    bash \
    goldvip-telemetry-packages \
    ifmetric \
    iputils \
    procps \
    iproute2 \
    python3-boto3 \
    python3-scapy \
    python3-requests \
    python3-rticonnextdds-connector \
    aws-iot-device-sdk-python-v2 \
    greengrass-bin \
    ntp ntpq \
"

do_install() {
    install -d ${DESTDIR}/cloud-gw
    install -m 0755 ${S}/cloud-gw/greengrass_provision.py ${DESTDIR}/cloud-gw
    install -m 0755 ${S}/cloud-gw/client_device_provision.py ${DESTDIR}/cloud-gw
    install -m 0755 ${S}/cloud-gw/utils.py ${DESTDIR}/cloud-gw
    install -m 0755 ${S}/cloud-gw/ggv2_deployment_configurations.json ${DESTDIR}/cloud-gw

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/cloud-gw/greengrass-functions/service/greengrass ${D}${sysconfdir}/init.d/greengrass
}

# set update-rc.d parameters
INITSCRIPT_NAME = "greengrass"
INITSCRIPT_PARAMS = "defaults 70"

FILES:${PN} += " \
    /home/root/cloud-gw/ \
    /etc/init.d/greengrass \
"
