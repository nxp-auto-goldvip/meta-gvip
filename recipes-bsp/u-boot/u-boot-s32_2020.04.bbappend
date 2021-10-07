
do_install_append() {
	install -d ${DEPLOY_DIR_IMAGE}
	cp -f ${B}/${config}/u-boot.bin ${DEPLOY_DIR_IMAGE}/u-boot.bin
}
