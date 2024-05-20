# Copyright 2024 NXP

require recipes-connectivity/nxp-wlan-sdk/nxp-wlan-sdk-s32.inc

# Autoload module
KERNEL_MODULE_AUTOLOAD += "moal"
# Autoprobe module
KERNEL_MODULE_PROBECONF += "moal"
module_conf_moal = "options moal mod_para=nxp/wifi_mod_para.conf"

FILES:${PN} += "${sysconfdir}/modules-load.d/*"
