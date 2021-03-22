FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# add the required kernel configs for GVIP
DELTA_KERNEL_DEFCONFIG_append = " \
	greengrass.cfg \
	gvip.cfg \
	realtek_wifi.cfg \
"

SRC_URI_append = " \
	file://build/greengrass.cfg \
	file://build/gvip.cfg \
	file://build/realtek_wifi.cfg \
"
