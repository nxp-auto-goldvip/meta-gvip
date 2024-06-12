SUMMARY = "Gold VIP (Vehicle Integration Platform) Bootloader"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

inherit logging
inherit deploy

DEPENDS += "xxd-native"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_BOOTLOADER_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_BOOTLOADER_BIN ?= "boot-loader"
GOLDVIP_BOOTLOADER_CFG_BIN ?= "Bootloader_Configuration.bin"

GOLDVIP_BOOTLOADER_CFG_JSON ?= "Bootloader_Configuration.json"
FIP_BIN ?= "fip.s32-sdcard"
IVT_APP_LOAD_ENTRY_OFFSET ?= "4612"

SRC_URI = " \
    file://${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_BIN} \
    ${@bb.utils.contains('ENABLE_DYNAMIC_BOOT_CONFIG', 'true', 'file://${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN}', '', d)} \
    ${@bb.utils.contains('ENABLE_DYNAMIC_BOOT_CONFIG', 'true', 'file://${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_JSON}', '', d)} \
    file://boot_config.py \
"

# tell yocto not to strip our binaries
INHIBIT_PACKAGE_STRIP = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_update_bootloader_cfg[depends] += "arm-trusted-firmware:do_deploy"

do_update_bootloader_cfg() {
    if [ "${ENABLE_DYNAMIC_BOOT_CONFIG}" = "true" ]; then
        mv ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN} ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/Original_${GOLDVIP_BOOTLOADER_CFG_BIN}

        NEW_LOAD_ADDRESS=0x$(xxd -plain -e -s ${IVT_APP_LOAD_ENTRY_OFFSET} -l 4 ${DEPLOY_DIR_IMAGE}/${FIP_BIN} | cut -d' ' -f2)

        python3 ${WORKDIR}/boot_config.py \
            -g ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR} \
            -v "Core-0 Image-0 RamAddress=${NEW_LOAD_ADDRESS}"

        diff ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/Original_${GOLDVIP_BOOTLOADER_CFG_BIN} ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN} || \
            bbwarn "Bootloader_Configuration.bin has changed, new A53 Load Address: ${NEW_LOAD_ADDRESS}"
    fi
}

do_install() {
    install -d ${D}/boot
    install -m 0644 "${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_BIN}" ${D}/boot

    if [ "${ENABLE_DYNAMIC_BOOT_CONFIG}" = "true" ]; then 
        install -m 0644 "${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN}" ${D}/boot
    fi
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${D}/boot/${GOLDVIP_BOOTLOADER_BIN} ${DEPLOYDIR}/${GOLDVIP_BOOTLOADER_BIN}

    if [ "${ENABLE_DYNAMIC_BOOT_CONFIG}" = "true" ]; then
        install -m 0644 ${D}/boot/${GOLDVIP_BOOTLOADER_CFG_BIN} ${DEPLOYDIR}/${GOLDVIP_BOOTLOADER_CFG_BIN}
    fi
}

addtask do_update_bootloader_cfg after do_prepare_recipe_sysroot before do_install
addtask do_deploy after do_install

FILES:${PN} += "/boot/${GOLDVIP_BOOTLOADER_BIN}"
FILES:${PN} += "${@bb.utils.contains('ENABLE_DYNAMIC_BOOT_CONFIG', 'true', '/boot/${GOLDVIP_BOOTLOADER_CFG_BIN}', '', d)}"
