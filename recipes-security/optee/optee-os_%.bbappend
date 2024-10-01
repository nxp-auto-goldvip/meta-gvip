# Copyright 2024 NXP

DEPENDS:append = " hse-firmware"

EXTRA_OEMAKE += "\
    CFG_RPMB_FS=y \
    CFG_RPMB_WRITE_KEY=y \
    CFG_REE_FS=n \
    CFG_NXP_HSE=y \
    CFG_NXP_HSE_FWDIR=${STAGING_INCDIR}/hse-interface \
"