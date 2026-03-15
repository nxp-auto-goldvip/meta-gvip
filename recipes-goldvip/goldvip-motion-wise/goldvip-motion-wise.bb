SUMMARY = "Gold VIP (Vehicle Integration Platform) Motion Wise DDS Demo"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/motion-wise/GoldVIP_MPU_Subscriber"

inherit bin_package

# Disable automatic shared library dependency detection
SKIP_FILEDEPS:${PN} = "1"

INSANE_SKIP:${PN} += "already-stripped ldflags file-rdeps"

do_install() {
    # Install application
    install -d ${D}/root/motion-wise
    install -m 0755 ${S}/build/GoldVIP_MPU_Subscriber ${D}/root/motion-wise/
    install -m 0755 ${S}/build/run_dds_subscriber.sh ${D}/root/motion-wise/
    install -m 0644 ${S}/cfg/configMpuApp_aux0sl.xml ${D}/root/motion-wise/

    # Libraries
    install -d ${D}${libdir}/motion-wise
    install -m 0755 ${S}/linux-prebuilt/aarch64/lib/*.so* ${D}${libdir}/motion-wise/ || true
    install -m 0644 ${S}/linux-prebuilt/aarch64/lib/*.a ${D}${libdir}/motion-wise/ || true

    # pkgconfig
    install -d ${D}${libdir}/motion-wise/pkgconfig
    install -m 0644 ${S}/linux-prebuilt/aarch64/lib/pkgconfig/*.pc \
        ${D}${libdir}/motion-wise/pkgconfig/ || true

    # CMake files
    for dir in cpptoml CycloneDDS CycloneDDS/idlc \
               CycloneDDS-CXX CycloneDDS-CXX/idlcxx \
               iceoryx_binding_c iceoryx_hoofs iceoryx_posh \
               zenohpico ZettaDDS; do
        install -d ${D}${libdir}/motion-wise/cmake/$dir
        install -m 0644 ${S}/linux-prebuilt/aarch64/lib/cmake/$dir/*.cmake \
            ${D}${libdir}/motion-wise/cmake/$dir/ 2>/dev/null || true
    done
}

FILES:${PN} += "/motion-wise"
FILES:${PN} += "${libdir}/motion-wise/*.so*"
FILES:${PN} += "${libdir}/motion-wise/pkgconfig"
FILES:${PN} += "${libdir}/motion-wise/cmake"

FILES:${PN}-staticdev = "${libdir}/motion-wise/*.a"
