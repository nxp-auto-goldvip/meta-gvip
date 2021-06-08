SUMMARY = "Gold VIP (Vehicle Integration Platform) CAN Gateway"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

inherit deploy

GOLDVIP_CAN_GW_DIR ?= "."
GOLDVIP_CAN_GW_BIN ?= "can-gw.bin"

SRC_URI = "file://${GOLDVIP_CAN_GW_DIR}/${GOLDVIP_CAN_GW_BIN}"

# tell yocto not to strip our binary
INHIBIT_PACKAGE_STRIP = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d ${D}/boot
	install -m 0644 "${GOLDVIP_CAN_GW_DIR}/${GOLDVIP_CAN_GW_BIN}" ${D}/boot
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${D}/boot/${GOLDVIP_CAN_GW_BIN} ${DEPLOYDIR}/${GOLDVIP_CAN_GW_BIN}
}

addtask do_deploy after do_install

FILES_${PN} += "/boot/${GOLDVIP_CAN_GW_BIN}"
