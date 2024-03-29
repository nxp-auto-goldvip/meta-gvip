# Adds moal (module abstraction layer) to the current kernel

require recipes-connectivity/nxp-wlan-sdk/nxp-wlan-sdk_git.inc

SUMMARY = "NXP Wi-Fi driver for module 88w8997/8987"

inherit module

EXTRA_OEMAKE += "-C ${STAGING_KERNEL_BUILDDIR} M=${S}"

# Autoload module
KERNEL_MODULE_AUTOLOAD += "moal"

# Autoprobe module 
KERNEL_MODULE_PROBECONF += "moal"
module_conf_moal = "options moal mod_para=nxp/wifi_mod_para.conf"
 
FILES:${PN} += "${sysconfdir}/modules-load.d/*"


