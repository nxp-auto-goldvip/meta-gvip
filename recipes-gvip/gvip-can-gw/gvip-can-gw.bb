SUMMARY = "VIP (Vehicle Integration Platform) CAN Gateway"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit deploy

GVIP_CAN_GW_DIR ?= "."
GVIP_CAN_GW_BIN ?= "can-gw.bin"

SRC_URI = "file://${GVIP_CAN_GW_DIR}/${GVIP_CAN_GW_BIN}"

# tell yocto not to strip our binary
INHIBIT_PACKAGE_STRIP = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d ${D}/boot
	install -m 0644 "${WORKDIR}/${GVIP_CAN_GW_DIR}/${GVIP_CAN_GW_BIN}" ${D}/boot
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${D}/boot/${GVIP_CAN_GW_BIN} ${DEPLOYDIR}/${GVIP_CAN_GW_BIN}
}

addtask do_deploy after do_install

FILES_${PN} += "/boot"
