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

generate_nxp_sdcard () {
	# Create partition table
	parted -s ${SDCARD} mklabel msdos
	parted -s ${SDCARD} unit KiB mkpart primary fat32 ${IMAGE_ROOTFS_ALIGNMENT} $(expr ${IMAGE_ROOTFS_ALIGNMENT} \+ ${BOOT_SPACE_ALIGNED})
	
	create_rootfs_partition 0 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_REAL}
	create_rootfs_partition 1 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA1_FILE}
	create_rootfs_partition 2 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA2_FILE}
	
	parted ${SDCARD} print

	# Fill optional Layerscape RCW into the boot block
	if [ -n "${SDCARD_RCW_NAME}" ]; then
	        dd if=${DEPLOY_DIR_IMAGE}/${SDCARD_RCW_NAME} of=${SDCARD} conv=notrunc seek=8 bs=512
	fi

	_burn_bootloader

	_generate_boot_image 1

	# Burn Partitions
	dd if=${WORKDIR}/boot.img of=${SDCARD} conv=notrunc,fsync seek=1 bs=$(expr ${IMAGE_ROOTFS_ALIGNMENT} \* 1024)
	
	write_rootfs_partition 0 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_REAL}
	write_rootfs_partition 1 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA1_FILE}
	write_rootfs_partition 2 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA2_FILE}
}

generate_sdcardimage_entry() {
        GSDE_IMAGE_FILE="$1"
        GSDE_IMAGE_FILE_OFFSET_NAME="$2"
        GSDE_IMAGE_FILE_OFFSET=$(printf "%d" "$3")
        GSDE_IMAGE="$4"
        if [ -n "${GSDE_IMAGE_FILE}" ]; then
                if [ -z "${GSDE_IMAGE_FILE_OFFSET}" ]; then
                        bberror "${GSDE_IMAGE_FILE_OFFSET_NAME} is undefined. To use the 'sdcard' image it needs to be defined as byte offset."
                        exit 1
                fi
                bbnote "Generating sdcard entry at ${GSDE_IMAGE_FILE_OFFSET} for ${GSDE_IMAGE_FILE}"
                dd if=${DEPLOY_DIR_IMAGE}/${GSDE_IMAGE_FILE} of=${GSDE_IMAGE} conv=notrunc,fsync bs=32K oflag=seek_bytes seek=${GSDE_IMAGE_FILE_OFFSET}
        fi
}

generate_nxp_sdcard () {
	# Create partition table
	parted -s ${SDCARD} mklabel msdos
	parted -s ${SDCARD} unit KiB mkpart primary fat32 ${IMAGE_ROOTFS_ALIGNMENT} $(expr ${IMAGE_ROOTFS_ALIGNMENT} \+ ${BOOT_SPACE_ALIGNED})
	
	create_rootfs_partition 0 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_REAL}
	create_rootfs_partition 1 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA1_FILE}
	create_rootfs_partition 2 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA2_FILE}
	
	parted ${SDCARD} print

	# Fill optional Layerscape RCW into the boot block
	if [ -n "${SDCARD_RCW_NAME}" ]; then
	        dd if=${DEPLOY_DIR_IMAGE}/${SDCARD_RCW_NAME} of=${SDCARD} conv=notrunc seek=8 bs=512
	fi

	_burn_bootloader

	_generate_boot_image 1

	# Burn Partitions
	dd if=${WORKDIR}/boot.img of=${SDCARD} conv=notrunc,fsync seek=1 bs=$(expr ${IMAGE_ROOTFS_ALIGNMENT} \* 1024)
	
	write_rootfs_partition 0 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_REAL}
	write_rootfs_partition 1 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA1_FILE}
	write_rootfs_partition 2 ${SDCARD_PARTITION_MAX_SIZE} ${SDCARD_ROOTFS_EXTRA2_FILE}
}

IMAGE_CMD_sdcard () {

	if [ -n "${UBOOT_BOOTSPACE_OFFSET}" ]; then
		UBOOT_BOOTSPACE_OFFSET=$(printf "%d" ${UBOOT_BOOTSPACE_OFFSET})
	else
		UBOOT_BOOTSPACE_OFFSET=$(expr ${UBOOT_BOOTSPACE_SEEK} \* 512)
	fi
	
	if [ -n "${SDCARD_ROOTFS_EXTRA1_FILE}" ]; then
		SDCARD_PARTITION_MAX_SIZE="${SDCARD_ROOTFS_EXTRA1_SIZE}"
		if (( ${ROOTFS_SIZE} > ${SDCARD_PARTITION_MAX_SIZE} )); then
			SDCARD_PARTITION_MAX_SIZE="${ROOTFS_SIZE}"
		fi
	else
		SDCARD_PARTITION_MAX_SIZE="${ROOTFS_SIZE}"
	fi 
	
	# Align boot partition and calculate total SD card image size
	BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE} + ${BASE_IMAGE_ROOTFS_ALIGNMENT} - 1)
	BOOT_SPACE_ALIGNED=$(expr ${BOOT_SPACE_ALIGNED} - ${BOOT_SPACE_ALIGNED} % ${BASE_IMAGE_ROOTFS_ALIGNMENT})

	# If the size has not been preset, we default to flash image
	# sizes if available turned into [KiB] or to a hardcoded mini
	# default of 4MB.
	if [ -z "${IMAGE_ROOTFS_ALIGNMENT}" ]; then
		if [ -n "${FLASHIMAGE_SIZE}" ]; then
			IMAGE_ROOTFS_ALIGNMENT=$(expr ${FLASHIMAGE_SIZE} \* 1024)
		else
			IMAGE_ROOTFS_ALIGNMENT=${BASE_IMAGE_ROOTFS_ALIGNMENT}
		fi
	fi
	
	SDCARD_SIZE=$(expr ${IMAGE_ROOTFS_ALIGNMENT} + ${BOOT_SPACE_ALIGNED})
	if [ -n "${SDCARD_ROOTFS_REAL}" ]; then
		SDCARD_SIZE=$(expr ${SDCARD_SIZE} + ${SDCARD_PARTITION_MAX_SIZE} + ${BASE_IMAGE_ROOTFS_ALIGNMENT})
	fi
	if [ -n "${SDCARD_ROOTFS_EXTRA1_FILE}" ]; then
		SDCARD_SIZE=$(expr ${SDCARD_SIZE} + ${SDCARD_PARTITION_MAX_SIZE} + ${BASE_IMAGE_ROOTFS_ALIGNMENT})
	fi
	if [ -n "${SDCARD_ROOTFS_EXTRA2_FILE}" ]; then
		SDCARD_SIZE=$(expr ${SDCARD_SIZE} + ${SDCARD_PARTITION_MAX_SIZE} + ${BASE_IMAGE_ROOTFS_ALIGNMENT})
	fi

	cd ${IMGDEPLOYDIR}

	# Initialize a sparse file
	dd if=/dev/zero of=${SDCARD} bs=1 count=0 seek=$(expr 1024 \* ${SDCARD_SIZE})

	# Additional elements for the raw image, copying the approach of the flashimage class
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA1_FILE}" "SDCARDIMAGE_EXTRA1_OFFSET" "${SDCARDIMAGE_EXTRA1_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA2_FILE}" "SDCARDIMAGE_EXTRA2_OFFSET" "${SDCARDIMAGE_EXTRA2_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA3_FILE}" "SDCARDIMAGE_EXTRA3_OFFSET" "${SDCARDIMAGE_EXTRA3_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA4_FILE}" "SDCARDIMAGE_EXTRA4_OFFSET" "${SDCARDIMAGE_EXTRA4_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA5_FILE}" "SDCARDIMAGE_EXTRA5_OFFSET" "${SDCARDIMAGE_EXTRA5_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA6_FILE}" "SDCARDIMAGE_EXTRA6_OFFSET" "${SDCARDIMAGE_EXTRA6_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA7_FILE}" "SDCARDIMAGE_EXTRA7_OFFSET" "${SDCARDIMAGE_EXTRA7_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA8_FILE}" "SDCARDIMAGE_EXTRA8_OFFSET" "${SDCARDIMAGE_EXTRA8_OFFSET}" "${SDCARD}"
	generate_sdcardimage_entry "${SDCARDIMAGE_EXTRA9_FILE}" "SDCARDIMAGE_EXTRA9_OFFSET" "${SDCARDIMAGE_EXTRA9_OFFSET}" "${SDCARD}"

	${SDCARD_GENERATION_COMMAND}
	cd -
}
