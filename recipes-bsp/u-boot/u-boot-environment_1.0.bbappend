# Copyright 2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}/goldvip:"

DISTRO_ENV_INC_FILENAME = "${S}/goldvip-distro-env-macros.txt"

addtask do_create_distro_env_inc_fragment after do_patch before do_compile
do_compile[depends] += " u-boot-environment:do_create_distro_env_inc_fragment"
do_create_distro_env_inc_fragment[vardeps] += " DISTRO_FEATURES GOLDVIP_DYNAMIC_BOOTCONFIG"

do_create_distro_env_inc_fragment() {
    rm -f ${DISTRO_ENV_INC_FILENAME}
    echo "\
#define GOLDVIP_BOOTLOADER_ENABLED (${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-bootloader', '1', '0', d)})
#define GOLDVIP_GATEWAY_ENABLED (${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-gateway', '1', '0', d)})
#define GOLDVIP_BOOTCONFIG_ENABLED (${@oe.utils.vartrue('GOLDVIP_DYNAMIC_BOOTCONFIG', '1', '0', d)})
" >> ${DISTRO_ENV_INC_FILENAME}
}
