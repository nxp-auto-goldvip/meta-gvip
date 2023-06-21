#
# Copyright 2023 NXP
#

SUMMARY = "Embedded Microprocessor Benchmark Consortium Coremark"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=0a18b17ae63deaa8a595035f668aebe1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit module-base

SRC_URI = " \
    git://github.com/eembc/coremark.git;branch=main \
"
SRCREV = "eefc986ebd3452d6adde22eafaff3e5c859f29e4"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile () {
    oe_runmake compile
}

DESTDIR = "/home/root/benchmark"

do_install() {
    install -d ${D}${DESTDIR}
    install -m 0755 ${S}/coremark.exe ${D}${DESTDIR}/coremark.exe
}

FILES:${PN} += "${DESTDIR}"
