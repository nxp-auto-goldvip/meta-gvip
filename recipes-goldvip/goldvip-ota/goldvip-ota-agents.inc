SUMMARY = "Gold VIP (Vehicle Integration Platform) Update Agents"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${FSL_EULA_FILE};md5=${FSL_EULA_FILE_MD5SUM}"

inherit update-rc.d
inherit features_check
REQUIRED_DISTRO_FEATURES ?= "goldvip-ota"

RDEPENDS:${PN} += " \
    bash \
    busybox \
    e2fsprogs-mke2fs \
    jq \
    libcrypto \
    libssl \
    util-linux-mkfs \
"

DESTDIR = "/home/root/ota"

do_install() {
    install -d ${D}${DESTDIR}
    install -m 0755 ${ARTIFACTS_DIR}/goldvip_uas ${D}${DESTDIR}
    install -m 0755 ${ARTIFACTS_DIR}/*.sh ${D}${DESTDIR}
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${ARTIFACTS_DIR}/service/goldvip-update-agents ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/ota
    install -m 0755 ${ARTIFACTS_DIR}/conf/* ${D}${sysconfdir}/ota/

    # Copy Uptane initial data.
    install -d ${D}/data
    cp -R ${ARTIFACTS_DIR}/abqdata/* ${D}/data/

    for ua_defaults in ${D}/data/*_ua/factory_defaults; do
        cp -R ${ua_defaults}/* $(dirname ${ua_defaults})/.
    done

    # Select flavor of the config file, with or without hypervizor.
    # In the no hypervizor flavor, the linux vm update agent does nothing.
    ln -rs ${D}${sysconfdir}/ota/linuxvm_ua_${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'hv', 'nohv', d)}.conf ${D}${sysconfdir}/ota/linuxvm_ua.conf
}

# set update-rc.d parameters
INITSCRIPT_NAME = "goldvip-update-agents"
INITSCRIPT_PARAMS = "defaults 98"

FILES:${PN} += " \
    /home/root/ota/ \
    ${sysconfdir}/init.d/goldvip-update-agents \
    ${sysconfdir}/ota \
    /data \
"