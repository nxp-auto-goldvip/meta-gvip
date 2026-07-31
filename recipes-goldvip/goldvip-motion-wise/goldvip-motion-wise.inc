SUMMARY = "Gold VIP (Vehicle Integration Platform) Motion Wise Communication libraries"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

MOTION_WISE_PKG ?= "ZettaAuto-v2.0.0"
MOTION_WISE_DIR ?= "${ARTIFACTS_DIR}/${MOTION_WISE_PKG}"
MOTION_WISE_LIB_DIR ?= "${MOTION_WISE_DIR}/product_package/ZettaAuto/MPU/linux-prebuilt/aarch64/lib"

# Disable automatic shared library dependency detection.
SKIP_FILEDEPS:${PN} = "1"

INSANE_SKIP:${PN} += "already-stripped ldflags file-rdeps"

do_install() {
    # Libraries
    install -d ${D}${libdir}/motion-wise
    install -m 0644 ${MOTION_WISE_LIB_DIR}/*.a ${D}${libdir}/motion-wise/ || true
    find ${MOTION_WISE_LIB_DIR} -maxdepth 1 -name "*.so*" ! -name "libcycloneddsidl*" \
        -exec install -m 0755 {} ${D}${libdir}/motion-wise/ \;

    # pkgconfig
    install -d ${D}${libdir}/motion-wise/pkgconfig
    install -m 0644 ${MOTION_WISE_LIB_DIR}/pkgconfig/*.pc ${D}${libdir}/motion-wise/pkgconfig/ || true

    # CMake files
    for dir in cpptoml CycloneDDS CycloneDDS/idlc CycloneDDS-CXX CycloneDDS-CXX/idlcxx \
               iceoryx_binding_c iceoryx_hoofs iceoryx_posh zenohpico ZettaDDS; do
        install -d ${D}${libdir}/motion-wise/cmake/$dir
        install -m 0644 ${MOTION_WISE_LIB_DIR}/cmake/$dir/*.cmake \
            ${D}${libdir}/motion-wise/cmake/$dir/ 2>/dev/null || true
    done
}

FILES:${PN} += " \
    ${libdir}/motion-wise \
    ${libdir}/motion-wise/*.so* \
    ${libdir}/motion-wise/pkgconfig \
    ${libdir}/motion-wise/cmake \
"

FILES:${PN}-staticdev = " \
    ${libdir}/motion-wise/*.a \
"