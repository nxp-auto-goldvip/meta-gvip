SUMMARY = "Plotly javascript library"
HOMEPAGE = "https://plotly.com/javascript/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f795602ad21fc98c67427e0b8255549"

SRC_URI = "git://github.com/plotly/plotly.js.git;protocol=https;branch=master"
SRCREV = "68a4917e8967bb021929e819453ee84650f7bf6f"

S = "${WORKDIR}/git"
DESTDIR = "/home/root/telemetry-server/static"

do_install() {
        install -d ${D}/${DESTDIR}
        install -m 0777 ${S}/dist/plotly.min.js ${D}/${DESTDIR}/plotly-2.12.1.min.js
}

FILES:${PN} += "${DESTDIR}"
