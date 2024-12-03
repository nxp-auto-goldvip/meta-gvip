SUMMARY = "Gold VIP (Vehicle Integration Platform) Bootloader"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

inherit logging deploy python3native

DEPENDS += "xxd-native python3-pycryptodome-native"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_BOOTLOADER_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_BOOTLOADER_BIN ?= "boot-loader"
GOLDVIP_BOOTLOADER_CFG_BIN ?= "Bootloader_Configuration.bin"

GOLDVIP_BOOTLOADER_CFG_JSON ?= "Bootloader_Configuration.json"
BL2_BIN ?= "bl2_w_dtb.s32-sdcard"
IVT_APP_LOAD_ENTRY_OFFSET ?= "4612"

SRC_URI = " \
    file://${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_BIN} \
    ${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', 'file://${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN}', '', d)} \
    ${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', 'file://${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_JSON}', '', d)} \
    file://boot_config.py \
    file://image_signer.py \
    file://rsa_2048_key.pem \
"

# tell yocto not to strip our binaries
INHIBIT_PACKAGE_STRIP = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_update_bootloader_cfg[depends] += "arm-trusted-firmware:do_deploy"

do_update_bootloader_cfg() {
    if [ "${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', 'true', 'false', d)}" = "true" ]; then
        mv ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN} ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/Original_${GOLDVIP_BOOTLOADER_CFG_BIN}

        NEW_LOAD_ADDRESS=0x$(xxd -plain -e -s ${IVT_APP_LOAD_ENTRY_OFFSET} -l 4 ${DEPLOY_DIR_IMAGE}/${BL2_BIN} | cut -d' ' -f2)

        python3 ${WORKDIR}/boot_config.py \
            -g ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR} \
            -v "Core-0 Image-0 RamAddress=${NEW_LOAD_ADDRESS}"

        python3 ${WORKDIR}/image_signer.py \
            -i ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/Bootloader_Configuration.bin \
            -o ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/Bootloader_Configuration.bin \
            -a RSA -k ${WORKDIR}/rsa_2048_key.pem

        diff ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/Original_${GOLDVIP_BOOTLOADER_CFG_BIN} ${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN} || \
            bbwarn "Bootloader_Configuration.bin has changed, new A53 Load Address: ${NEW_LOAD_ADDRESS}"
    fi
}

do_install() {
    install -d ${D}/boot
    install -m 0644 "${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_BIN}" ${D}/boot

    if [ "${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', 'true', 'false', d)}" = "true" ]; then
        install -m 0644 "${WORKDIR}/${GOLDVIP_BOOTLOADER_DIR}/${GOLDVIP_BOOTLOADER_CFG_BIN}" ${D}/boot
    fi
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${D}/boot/${GOLDVIP_BOOTLOADER_BIN} ${DEPLOYDIR}/${GOLDVIP_BOOTLOADER_BIN}

    if [ "${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', 'true', 'false', d)}" = "true" ]; then
        install -m 0644 ${D}/boot/${GOLDVIP_BOOTLOADER_CFG_BIN} ${DEPLOYDIR}/${GOLDVIP_BOOTLOADER_CFG_BIN}
    fi
}

addtask do_update_bootloader_cfg after do_prepare_recipe_sysroot before do_install
addtask do_deploy after do_install

FILES:${PN} += "/boot/${GOLDVIP_BOOTLOADER_BIN}"
FILES:${PN} += "${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', '/boot/${GOLDVIP_BOOTLOADER_CFG_BIN}', '', d)}"
