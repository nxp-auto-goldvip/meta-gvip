# Copyright 2023-2024 NXP

# Stick with the older HSE FW version.
HSE_VERSION = "0_2_51_0"

HSE_LIC_MD5:s32g2 = "cc5fb8fd09eb58b9e39127149404a283"
HSE_LIC_MD5:s32g3 = "cc5fb8fd09eb58b9e39127149404a283"

# Set a dummy value for the SoC Revision on S32G2.
HSE_SOC_REV:s32g2 ?= "rev2.1"
