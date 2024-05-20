# Copyright 2023-2024 NXP

# Stick with the older HSE FW version.
HSE_VERSION = "0_2_22_0"

HSE_LIC_MD5:s32g2 = "0474bb8a03b7bc0ac59e9331d5be687f"
HSE_LIC_MD5:s32g3 = "a1bda359fc5cdcfca04f84834841a5ca"

# Set a dummy value for the SoC Revision on S32G2.
HSE_SOC_REV:s32g2 ?= "rev2.1"
