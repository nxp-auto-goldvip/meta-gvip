SUMMARY = "Gold VIP (Vehicle Integration Platform) Update Agents"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=7dbfb74206189d683981a89b8912ce5d"

inherit update-rc.d
inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

GOLDVIP_BINARIES_DIR ?= "."
GOLDVIP_OTA_DIR ?= "${GOLDVIP_BINARIES_DIR}"
GOLDVIP_UPDATE_AGENTS_TARBALL ?= "goldvip_uas.tgz"

SRC_URI = " \
    file://${GOLDVIP_OTA_DIR}/${GOLDVIP_UPDATE_AGENTS_TARBALL} \
"

S = "${WORKDIR}/goldvip_uas"
RDEPENDS_${PN} += " \
    bash \
    busybox \
    e2fsprogs-mke2fs \
    jq \
    libcrypto \
    libssl \
    util-linux-mkfs \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}/home/root/ota/
    install -m 0755 ${S}/goldvip_uas ${D}/home/root/ota/
    install -m 0755 ${S}/*.sh ${D}/home/root/ota/
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/service/goldvip-update-agents ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/ota
    install -m 0755 ${S}/conf/* ${D}${sysconfdir}/ota/

    # Copy Uptane initial data.
    install -d ${D}/data
    cp -R ${S}/abqdata/* ${D}/data/

    for ua_defaults in ${D}/data/*_ua/factory_defaults; do
        cp -R ${ua_defaults}/* $(dirname ${ua_defaults})/.
    done
}

# set update-rc.d parameters
INITSCRIPT_NAME = "goldvip-update-agents"
INITSCRIPT_PARAMS = "defaults 98"

FILES_${PN} += " \
    /home/root/ota/ \
    ${sysconfdir}/init.d/goldvip-update-agents \
    ${sysconfdir}/ota \
    /data \
"
