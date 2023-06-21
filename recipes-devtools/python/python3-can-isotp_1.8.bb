# -*- mode: Conf; -*-
SUMMARY = "Python CAN ISOTP implementation"
HOMEPAGE = "https://github.com/pylessard/python-can-isotp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4c030137c54ce73fc69b9c990b62b5c1"

SRC_URI = "git://github.com/pylessard/python-can-isotp.git;protocol=https;tag=v1.8"
S = "${WORKDIR}/git"

inherit setuptools3

RDEPENDS:${PN} += " python3-can \
	python3-cantools \
	python3-can \
	python3-bitstruct \
	python3-diskcache \
	python3-msgpack \
	python3-packaging \
	python3-textparser \
"
