require conf/distro/fsl-auto.conf

DISTRO_NAME = "GoldVIP"
DISTRO_VERSION = "${GOLDVIP_RELEASE} (based on Auto Linux BSP ${ALB_RELEASE})"

# Features specific to the GoldVIP distros
DISTRO_FEATURES:append = " pfe-slave"
DISTRO_FEATURES:append = " goldvip-bootloader"
DISTRO_FEATURES:append = " goldvip-cloud"
DISTRO_FEATURES:append = " goldvip-crypto"
DISTRO_FEATURES:append = " goldvip-containerization seccomp"
DISTRO_FEATURES:append = " goldvip-dds"
DISTRO_FEATURES:append = " goldvip-gateway"
DISTRO_FEATURES:append = " goldvip-ota"
DISTRO_FEATURES:append = " goldvip-benchmark"
DISTRO_FEATURES:append = " goldvip-telemetry-server"
DISTRO_FEATURES:append = " optee"

SJA1110_UC_FW ?= "${GOLDVIP_BINARIES_DIR}/sja1110_uc.bin"
SJA1110_SWITCH_FW ?= "${GOLDVIP_BINARIES_DIR}/sja1110_switch.bin"

DISTRO_FEATURES:remove = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED:remove = " sysvinit"
VIRTUAL-RUNTIME_init_manager = "sysvinit"
VIRTUAL-RUNTIME_initscripts = "initscripts"

PREFERRED_PROVIDER_virtual/runc = "runc-opencontainers"

GOLDVIP_DYNAMIC_BOOTCONFIG ?= "true"
# By default, Serdes1 is configured from the RT apps, so the Linux domain should not configure it.
GOLDVIP_DISABLE_SERDES1_CONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'true', 'false', d)}"

# TF-A secure-boot variables. The same RSA2048 private key used to authenticate the boot table is
# also used to auth the TF-A BL2 and subsequent images.
# The same RSA2048 private key is also used to authenticate the DDR FW.
RSA_PRIV_BL2 ?= "${GOLDVIP_RSA_PRIVKEY_FILE}"
BL2_HANDLE ?= "0x010700"
RSA_PRIV_BL31 ?= "${GOLDVIP_RSA_PRIVKEY_FILE}"
BL31_HANDLE ?= "${BL2_HANDLE}"
RSA_PRIV_BL32 ?= "${GOLDVIP_RSA_PRIVKEY_FILE}"
BL32_HANDLE ?= "${BL2_HANDLE}"
RSA_PRIV_BL33 ?= "${GOLDVIP_RSA_PRIVKEY_FILE}"
BL33_HANDLE ?= "${BL2_HANDLE}"
RSA_PRIV_DDRFW ?= "${GOLDVIP_RSA_PRIVKEY_FILE}"
DDRFW_HANDLE ?= "${BL2_HANDLE}"