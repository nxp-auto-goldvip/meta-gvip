# Sysvinit service wrapper
inherit update-rc.d

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI += "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH};name=gvip;destsuffix=gvip"
SRCREV_gvip = "${AUTOREV}"
SRCREV_FORMAT = "fwe_gvip"
SRCREV_fwe = "05a9d52159c78721f14aa70f641df557a4133bc8"

DESTDIR_GVIP_SCRIPTS = "${D}/home/root/fleetwise/"

do_install_append () {	
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/gvip/fleetwise/edge/service/aws-iot-fwe ${D}${sysconfdir}/init.d/aws-iot-fwe
	
	install -d ${DESTDIR_GVIP_SCRIPTS}
	install -m 0755 ${WORKDIR}/gvip/fleetwise/deploy/*.py ${DESTDIR_GVIP_SCRIPTS}
	install -m 0644 ${WORKDIR}/gvip/cloud-gw/utils.py ${DESTDIR_GVIP_SCRIPTS}
}

# set update-rc.d parameters
INITSCRIPT_NAME = "aws-iot-fwe"
INITSCRIPT_PARAMS = "defaults 99"

FILES_${PN} += "${sysconfdir}"
FILES_${PN} += "${sysconfdir}/init.d/aws-iot-fwe"
FILES_${PN} += "/home/root/fleetwise/*"
