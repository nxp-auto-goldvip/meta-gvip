FILESEXTRAPATHS_prepend := "${THISDIR}/env:"
SRC_URI += " file://0001-set-dom0-mem.patch;patch=1 "

do_install_append() {
	install -d ${DEPLOY_DIR_IMAGE}
	cp -f ${B}/${config}/u-boot.bin ${DEPLOY_DIR_IMAGE}/u-boot.bin
}
