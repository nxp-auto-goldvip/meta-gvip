SUMMARY = "Gold VIP (Vehicle Integration Platform) IPCF character device"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=5ceedb30e981c71faacf42522463f3ff"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

inherit module

S = "${WORKDIR}/git/ipcf"
DESTDIR="${D}"

RDEPENDS_{PN} = "kernel-module-ipc-shm-dev"
DEPENDS = "ipc-shm"

EXTRA_OEMAKE_append = " -C . INSTALL_DIR=${DESTDIR} IPC_SHM_DEV_PATH=${STAGING_INCDIR}/ipc-shm KERNELDIR=${KBUILD_OUTPUT} "

MODULES_MODULE_SYMVERS_LOCATION = "."

PROVIDES += "kernel-module-ipc-chdev${KERNEL_MODULE_PACKAGE_SUFFIX}"
RPROVIDES_${PN} += "kernel-module-ipc-chdev${KERNEL_MODULE_PACKAGE_SUFFIX}"

KERNEL_MODULE_AUTOLOAD += "ipc-chdev"

FILES_${PN} += "${base_libdir}/*"
FILES_${PN} += "${sysconfdir}/modules-load.d/*" 
