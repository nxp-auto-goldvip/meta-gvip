require conf/distro/fsl-auto.conf

DISTRO_NAME = "GoldVIP"
DISTRO_VERSION = "${GOLDVIP_RELEASE} (based on Auto Linux BSP ${ALB_RELEASE})"

# Features specific to the GoldVIP distros
DISTRO_FEATURES:append = " pfe-slave"
DISTRO_FEATURES:append = " goldvip-bootloader"
DISTRO_FEATURES:append = " goldvip-crypto"
DISTRO_FEATURES:append = " goldvip-containerization seccomp"
DISTRO_FEATURES:append = " goldvip-dds"
DISTRO_FEATURES:append = " goldvip-gateway"
DISTRO_FEATURES:append = " goldvip-ml"
DISTRO_FEATURES:append = " goldvip-ota"
DISTRO_FEATURES:append = " goldvip-benchmark"
DISTRO_FEATURES:append = " goldvip-telemetry-server"
DISTRO_FEATURES:append = " goldvip-adaptive-autosar"
DISTRO_FEATURES:append = " optee"

SJA1110_UC_FW ?= "${GOLDVIP_BINARIES_DIR}/sja1110_uc.bin"
SJA1110_SWITCH_FW ?= "${GOLDVIP_BINARIES_DIR}/sja1110_switch.bin"
ENABLE_DYNAMIC_BOOT_CONFIG ?= "true"

DISTRO_FEATURES:remove = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED:remove = " sysvinit"
VIRTUAL-RUNTIME_init_manager = "sysvinit"
VIRTUAL-RUNTIME_initscripts = "initscripts"

PREFERRED_PROVIDER_virtual/java-initial-native = "cacao-initial-native"
PREFERRED_PROVIDER_virtual/java-native = "cacao-native"
PREFERRED_VERSION_cacao-native = "git"

PREFERRED_PROVIDER_virtual/runc = "runc-opencontainers"
PREFERRED_VERSION_python3-boto3 ?= "1.28.77"
PREFERRED_VERSION_python3-botocore ?= "1.31.77"
PREFERRED_VERSION_greengrass-bin ?= "2.9.1"
PREFERRED_VERSION_k3s ?= "v1.25.8+k3s1"
PREFERRED_VERSION_sloci-image ?= "v1.0.2+git%"

