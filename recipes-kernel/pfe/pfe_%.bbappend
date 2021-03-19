#
# Copyright 2021 NXP
#

# Run pfe driver with cut version 2.0, previously it run with cut version 1.1.
EXTRA_OEMAKE_remove = "PFE_CFG_IP_VERSION=PFE_CFG_IP_VERSION_NPU_7_14"
