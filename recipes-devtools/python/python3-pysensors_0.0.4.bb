SUMMARY = "Python bindings to libsensors (via ctypes)"
HOMEPAGE = "http://pypi.python.org/pypi/PySensors/"
AUTHOR = "Marc Rintsch <marc@rintsch.de>"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/LGPL-2.1-or-later;md5=2a4f4fd2128ea2f65047ee63fbca9f68"

SRC_URI[md5sum] = "af5ca440297b45bd440d32a7c8341c0f"
SRC_URI[sha256sum] = "beb0def410d29ee46fe196a7811124772abf84cbe3a0d8b01d80b81fba31dae5"

PYPI_PACKAGE = "PySensors"
inherit pypi setuptools3

BBCLASSEXTEND += "native"
