require recipes-extended/xen-examples/xen-examples.inc

CFG_NAME = "config_${MACHINE}_goldvip"

SRC_URI += "file://config_${MACHINE}_goldvip"
FILESEXTRAPATHS:prepend = "${THISDIR}/xen-examples:"

