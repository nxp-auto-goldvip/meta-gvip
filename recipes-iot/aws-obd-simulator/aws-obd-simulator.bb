DESCRIPTION = "AWS IoT FleetWise Can simulator" 
LICENSE = "ASL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79ba3a8bc4e89e76f90b0dadbf304749"

inherit update-rc.d

GOLDVIP_URL ?= "git://source.codeaurora.org/external/autobsps32/goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

FILESEXTRAPATHS_prepend:="${THISDIR}/files:"

SRC_URI = "git://github.com/aws/aws-iot-fleetwise-edge.git;protocol=https;branch=main;name=fwe \
           ${GOLDVIP_URL};branch=${GOLDVIP_BRANCH};name=gvip;destsuffix=gvip \
           file://0001-recreate-isotp-socket-in-case-of-failure.patch;patch=1 \
"
SRCREV_fwe = "cb818bbf334e5f872f583740912aa1c33247fed4"
SRCREV_gvip = "${AUTOREV}"
SRCREV_FORMAT = "fwe_gvip"

S = "${WORKDIR}/git"

RDEPENDS_${PN} += "\
    python3-core \
    python3-can-isotp \
    goldvip-can-setup \
    goldvip-gateway \
    "

do_install () {
    install -d ${D}${bindir}/aws-obd-simulator
    install -m 0755 ${S}/tools/cansim/canigen.py ${D}${bindir}/aws-obd-simulator/canigen.py
    install -m 0755 ${S}/tools/cansim/cansim.py ${D}${bindir}/aws-obd-simulator/cansim.py
    install -m 0600 ${S}/tools/cansim/hscan.dbc ${D}${bindir}/aws-obd-simulator/hscan.dbc
    install -m 0600 ${S}/tools/cansim/obd_config.json ${D}${bindir}/aws-obd-simulator/obd_config.json
    
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/gvip/fleetwise/obd-simulator/service/aws-obd-simulator ${D}${sysconfdir}/init.d/aws-obd-simulator
}

# set update-rc.d parameters
INITSCRIPT_NAME = "aws-obd-simulator"
INITSCRIPT_PARAMS = "defaults 98"

FILES_${PN} += "${bindir}/aws-obd-simulator/"
FILES_${PN} += "${sysconfdir}/init.d/aws-obd-simulator"

