#
# Copyright 2021-2023 NXP
#

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += " \
  file://0001-Fix-libfci-unresponsiveness-in-L2-bridge.patch;patch=1 \
"

# Enable PFE Master-Slave support
PFE_MASTER_OPTIONS = " PFE_CFG_MULTI_INSTANCE_SUPPORT=1 PFE_CFG_PFE_MASTER=1 "
