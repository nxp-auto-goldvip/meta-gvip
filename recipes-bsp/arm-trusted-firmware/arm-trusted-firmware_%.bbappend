# FIP QSPI OFFSET needs to be kept in sync with the Bootloader configuration.
# Bootloader will copy the BL2 image from FIP_QSPI_OFFSET + 0x200 (accounted 
# for FIP header) to SRAM. BL2, will then load the other bootloaders 
# (BL31, BL32, BL33) to SRAM/DDR

EXTRA_OEMAKE += " \
		FIP_QSPI_OFFSET=0x103400 \
		S32G_USE_LINFLEX_IN_BL31=1 \
		"

# Override flags which are used for the normal compilation phases when compiling for OTA.
GOLDVIP_ATF_OTA_FLAGS ?= "FIP_QSPI_OFFSET=0x603400"

do_generate_ota_artefact() {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS

	oe_runmake -C "${S}" clean

	oe_runmake -C "${S}" \
	    BL33="${DEPLOY_DIR_IMAGE}/u-boot.bin-sdcard" \
	    "${GOLDVIP_ATF_OTA_FLAGS}" \
	    MKIMAGE="${DEPLOY_DIR_IMAGE}/tools/mkimage-sdcard" all
	cp -vf "${ATF_BINARIES}/fip.s32" "${ATF_BINARIES}/fip.s32-sdcard-ota"
	
	# copy to deploy directory
	install -d ${DEPLOY_DIR_IMAGE}
	cp -vf "${ATF_BINARIES}/fip.s32-sdcard-ota" "${DEPLOY_DIR_IMAGE}/"
}

addtask do_generate_ota_artefact after do_deploy before do_build
