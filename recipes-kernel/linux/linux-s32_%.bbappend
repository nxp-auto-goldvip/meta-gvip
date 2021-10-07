FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# add the required kernel configs for GoldVIP
DELTA_KERNEL_DEFCONFIG_append = " \
	greengrass.cfg \
	goldvip.cfg \
	usb_network.cfg \
"

SRC_URI_append = " \
	file://build/greengrass.cfg \
	file://build/goldvip.cfg \
	file://build/usb_network.cfg \
	file://0001-xen-extend-xen_swiotlb_dma_ops-with-map_resource-and.patch;patch=1 \
"
