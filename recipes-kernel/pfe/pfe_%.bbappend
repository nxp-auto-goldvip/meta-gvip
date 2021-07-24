#
# Copyright 2021 NXP
#

# Run pfe driver with cut version 2.0, previously it run with cut version 1.1.
EXTRA_OEMAKE_remove = "PFE_CFG_IP_VERSION=PFE_CFG_IP_VERSION_NPU_7_14"

# Exclude (R)PROVIDES for the following tasks otherwise a hashbase mismatch is
# reported between the hash before kernel build (which triggers an empty kernel
# version in the (R)PROVIDES) and the hash after kernel build (using the updated
# kernel version)
do_populate_sysroot[vardepsexclude] += "PROVIDES"
do_populate_sysroot_setscene[vardepsexclude] += "PROVIDES"
do_package[vardepsexclude] += "PROVIDES RPROVIDES_${PN}"
