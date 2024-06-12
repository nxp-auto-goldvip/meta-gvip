SUMMARY = "Gold VIP (Vehicle Integration Platform) CAN Gateway"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit deploy

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_GATEWAY_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_GATEWAY_BIN ?= "goldvip-gateway.bin"

SRC_URI = "file://${GOLDVIP_GATEWAY_DIR}/${GOLDVIP_GATEWAY_BIN}"

# tell yocto not to strip our binary
INHIBIT_PACKAGE_STRIP = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d ${D}/boot
	install -m 0644 "${WORKDIR}/${GOLDVIP_GATEWAY_DIR}/${GOLDVIP_GATEWAY_BIN}" ${D}/boot
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${D}/boot/${GOLDVIP_GATEWAY_BIN} ${DEPLOYDIR}/${GOLDVIP_GATEWAY_BIN}
}

addtask do_deploy after do_install

FILES:${PN} += "/boot/${GOLDVIP_GATEWAY_BIN}"
