DESCRIPTION = "PKCS11 userspace module for HSE"
HOMEPAGE = "https://source.codeaurora.org/external/autobsps32/pkcs11-hse/"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.BSD;md5=f03611747c1e7d618ef405a8484ed48d"

# Dummy entry to keep the recipe parser happy if we don't use this recipe
NXP_FIRMWARE_LOCAL_DIR ?= "."

# Choosing the HSE FW directory based on the selected device
HSE_FWDIR_s32g2 ?= "${NXP_FIRMWARE_LOCAL_DIR}/HSE_FW_S32G2_0_1_0_5"
HSE_FWDIR_s32g3 ?= "${NXP_FIRMWARE_LOCAL_DIR}/HSE_FW_S32G3_0_0_21_0"
# Dummy entry
HSE_FWDIR ?= "${NXP_FIRMWARE_LOCAL_DIR}/HSE_FW_S32G"

# Default variables defined via meta-alb layer configuration
BRANCH ?= "${RELEASE_BASE}"
URL ?= "git://source.codeaurora.org/external/autobsps32/pkcs11-hse;protocol=https"
SRC_URI = "${URL};branch=${BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

RDEPENDS_${PN} += " openssl libp11"

EXTRA_OEMAKE = " \
	CROSS_COMPILE=${TARGET_PREFIX} \
	HSE_FWDIR=${HSE_FWDIR} \
	V=1 \
"

CFLAGS = " -shared -fPIC -Wall -fno-builtin --sysroot=${STAGING_DIR_TARGET}"
LDFLAGS = " --sysroot=${STAGING_DIR_TARGET}"

INSANE_SKIP_${PN} += " ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

do_install () {
    install -d ${D}${libdir}
    install -m 0755 ${B}/libpkcs-hse.so ${D}${libdir}
    install -m 0755 ${B}/libhse.so.1.0 ${D}${libdir}
    ln -sr ${D}${libdir}/libhse.so.1.0 ${D}${libdir}/libhse.so.1
}

FILES_${PN} += " \
    ${libdir}/* \
"

