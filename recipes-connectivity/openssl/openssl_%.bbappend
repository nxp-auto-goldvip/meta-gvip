FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Enable the AF_ALG engine.
SRC_URI += "file://0001-Enable-AF_ALG-engine.patch"

