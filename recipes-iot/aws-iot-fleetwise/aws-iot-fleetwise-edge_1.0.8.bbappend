# Copyright 2022-2023 NXP

inherit update-rc.d

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI += "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH};name=gvip;destsuffix=gvip"
SRCREV_gvip = "${AUTOREV}"
SRCREV_FORMAT = "fwe_gvip"
SRCREV_fwe = "0a882bde6ad75c143f96a89fa88c22932c244e5e"

DESTDIR_GVIP_SCRIPTS = "${D}/home/root/fleetwise/"

# Set make as designated build tool since ninja fails to build target fwe-proto
OECMAKE_GENERATOR = "Unix Makefiles"

# Add SysVinit wrapper service, along with other files needed to demonstrate this use case.
do_install:append () {	
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/gvip/fleetwise/edge/service/aws-iot-fwe ${D}${sysconfdir}/init.d/aws-iot-fwe
	
	install -d ${DESTDIR_GVIP_SCRIPTS}
	install -m 0755 ${WORKDIR}/gvip/fleetwise/deploy/*.py ${DESTDIR_GVIP_SCRIPTS}
	install -m 0644 ${WORKDIR}/gvip/cloud-gw/utils.py ${DESTDIR_GVIP_SCRIPTS}
}

# set update-rc.d parameters
INITSCRIPT_NAME = "aws-iot-fwe"
INITSCRIPT_PARAMS = "defaults 99"

FILES:${PN} += "${sysconfdir}"
FILES:${PN} += "${sysconfdir}/init.d/aws-iot-fwe"
FILES:${PN} += "/home/root/fleetwise/*"
