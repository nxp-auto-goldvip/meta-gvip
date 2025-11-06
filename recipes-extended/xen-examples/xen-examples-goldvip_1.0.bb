require recipes-extended/xen-examples/xen-examples.inc

CFG_NAME = "config_${MACHINE}_goldvip"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

SRC_URI += "file://xen-examples-goldvip"

do_post_unpack() {
    cp -vf ${WORKDIR}/xen-examples-goldvip/* ${WORKDIR}/xen-examples/
}

addtask post_unpack after do_unpack before do_patch
