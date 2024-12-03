SUMMARY = "Gold VIP (Vehicle Integration Platform) SD-card partitioning post install service"
DESCRIPTION = "First-boot only service that alters the partition table of the GoldVIP \
.sdcard image in order to increase the v2xdomu rootfs and create the slot B partition \
for the Linux VM OTA use-case. Meant to be run only once."
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

GOLDVIP_URL ?= "git://github.com/nxp-auto-goldvip/gvip;protocol=https"
GOLDVIP_BRANCH ?= "develop"

inherit update-rc.d

# The end offset (in bytes) in partition table until the domU partition is extended.
# Note: This is not the size of the final partition. For a 2GiB dom0 image, expect a
# final size between 5.5-6GiB for domU (depending on your storage medium size).
GOLDVIP_DOMU_PARTITION_END ?= "8589934592"

SRC_URI = "${GOLDVIP_URL};branch=${GOLDVIP_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/sdcard-partitioning"

RDEPENDS:${PN} += " \
    bash \
    e2fsprogs-resize2fs \
    parted \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/service/sdcard-partitioning ${D}${sysconfdir}/init.d/

    install -d ${D}${sysconfdir}/default
    install -m 0755 ${S}/conf/sdcard-partitioning ${D}${sysconfdir}/default

    # Update the SD-card partitioning configuration based on the enabled features.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'true', 'false', d)}; then
        sed -i -e 's|^\#\(RESIZE_V2XDOMU\)=.*|\1="true"|' ${D}${sysconfdir}/default/sdcard-partitioning
        sed -i -e 's|^\#\(V2XDOMU_PARTITION_END\)=.*|\1='"${GOLDVIP_DOMU_PARTITION_END}"'|' ${D}${sysconfdir}/default/sdcard-partitioning

        if ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-ota', 'true', 'false', d)}; then
            sed -i -e 's|^\#\(CREATE_OTA_PARTITION\)=.*|\1="true"|' ${D}${sysconfdir}/default/sdcard-partitioning
        fi
    fi
}

# Set the update-rc.d parameters - ensure this service gets picked-up before xencommons.
INITSCRIPT_NAME = "sdcard-partitioning"
INITSCRIPT_PARAMS = "start 30 S ."

FILES:${PN} += " \
    /etc/init.d/sdcard-partitioning \
    /etc/default/sdcard-partitioning \
"
