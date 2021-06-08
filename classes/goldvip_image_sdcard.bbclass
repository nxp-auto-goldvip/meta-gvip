inherit image_types_fsl_sdcard

SDCARDIMAGE_BOOT_EXTRA3 ?= ""
SDCARDIMAGE_BOOT_EXTRA3_FILE ?= ""
SDCARDIMAGE_BOOT_EXTRA4 = ""
SDCARDIMAGE_BOOT_EXTRA4_FILE = ""
SDCARDIMAGE_BOOT_EXTRA5 ?= ""
SDCARDIMAGE_BOOT_EXTRA5_FILE ?= ""
SDCARDIMAGE_BOOT_EXTRA6 ?= ""
SDCARDIMAGE_BOOT_EXTRA6_FILE ?= ""

do_image_sdcard[depends] += " ${@d.getVar('SDCARDIMAGE_BOOT_EXTRA3_FILE', True) and d.getVar('SDCARDIMAGE_BOOT_EXTRA3', True) + ':do_deploy' or ''} \
	${@d.getVar('SDCARDIMAGE_BOOT_EXTRA4_FILE', True) and d.getVar('SDCARDIMAGE_BOOT_EXTRA2', True) + ':do_deploy' or ''} \
	${@d.getVar('SDCARDIMAGE_BOOT_EXTRA5_FILE', True) and d.getVar('SDCARDIMAGE_BOOT_EXTRA2', True) + ':do_deploy' or ''} \
	${@d.getVar('SDCARDIMAGE_BOOT_EXTRA6_FILE', True) and d.getVar('SDCARDIMAGE_BOOT_EXTRA2', True) + ':do_deploy' or ''} "

#
# Generate the boot image with the boot scripts and required Device Tree
# files
_generate_boot_image() {
	local boot_part=$1

	# Create boot partition image
	BOOT_BLOCKS=$(LC_ALL=C parted -s ${SDCARD} unit b print \
	                  | awk "/ $boot_part / { print substr(\$4, 1, length(\$4 -1)) / 1024 }")

	# mkdosfs will sometimes use FAT16 when it is not appropriate,
	# resulting in a boot failure from SYSLINUX. Use FAT32 for
	# images larger than 512MB, otherwise let mkdosfs decide.
	if [ $(expr $BOOT_BLOCKS / 1024) -gt 512 ]; then
		FATSIZE="-F 32"
	fi

	rm -f ${WORKDIR}/boot.img
	mkfs.vfat -n "${BOOTDD_VOLUME_ID}" -S 512 ${FATSIZE} -C ${WORKDIR}/boot.img $BOOT_BLOCKS

	mcopy -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/${UBOOT_KERNEL_IMAGETYPE}-${MACHINE}.bin ::/${UBOOT_KERNEL_IMAGETYPE}

	# Copy boot scripts
	for item in ${BOOT_SCRIPTS}; do
		src=`echo $item | awk -F':' '{ print $1 }'`
		dst=`echo $item | awk -F':' '{ print $2 }'`

		mcopy -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/$src ::/$dst
	done

	# Copy device tree file
	if test -n "${KERNEL_DEVICETREE}"; then
		kernel_bin="`readlink ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${MACHINE}.bin`"
		for DTS_FILE in ${KERNEL_DEVICETREE}; do
			DTS_BASE_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
			DTB_PATH=""
			kernel_bin_for_dtb=""
			if [ -e "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.dtb" ]; then
				DTB_PATH="${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.dtb"
				kernel_bin_for_dtb="`readlink $DTB_PATH | sed "s,$DTS_BASE_NAME,${MACHINE},g;s,\.dtb$,.bin,g"`"
			else if [ -e "${DEPLOY_DIR_IMAGE}/${DTS_BASE_NAME}.dtb" ]; then
					DTB_PATH="${DEPLOY_DIR_IMAGE}/${DTS_BASE_NAME}.dtb"
					kernel_bin_for_dtb="`readlink $DTB_PATH | sed "s,$DTS_BASE_NAME,${KERNEL_IMAGETYPE},g;s,\.dtb$,.bin,g"`"
				fi
			fi
			if [ -n "$DTB_PATH" ]; then
				# match dtb and kernel image by timestamp
				if [ $kernel_bin = $kernel_bin_for_dtb ]; then
					mcopy -i ${WORKDIR}/boot.img -s "$DTB_PATH" ::/${DTS_BASE_NAME}.dtb
				fi
			else
				bbfatal "${DTS_FILE} does not exist."
			fi
		done
	fi

	# Copy extlinux.conf to images that have U-Boot Extlinux support.
	if [ "${UBOOT_EXTLINUX}" = "1" ]; then
		mmd -i ${WORKDIR}/boot.img ::/extlinux
		mcopy -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/extlinux.conf ::/extlinux/extlinux.conf
	fi

	# Add extra boot images in the SDCARD boot partition
	add_extra_boot_img "${SDCARDIMAGE_BOOT_EXTRA1_FILE}" "${WORKDIR}/boot.img"
	add_extra_boot_img "${SDCARDIMAGE_BOOT_EXTRA2_FILE}" "${WORKDIR}/boot.img"
	add_extra_boot_img "${SDCARDIMAGE_BOOT_EXTRA3_FILE}" "${WORKDIR}/boot.img"
	add_extra_boot_img "${SDCARDIMAGE_BOOT_EXTRA4_FILE}" "${WORKDIR}/boot.img"
	add_extra_boot_img "${SDCARDIMAGE_BOOT_EXTRA5_FILE}" "${WORKDIR}/boot.img"
	add_extra_boot_img "${SDCARDIMAGE_BOOT_EXTRA6_FILE}" "${WORKDIR}/boot.img"
}

