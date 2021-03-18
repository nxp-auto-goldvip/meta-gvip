SUMMARY = "JSON Matching Expressions"
AUTHOR = "James Saryerwinnie"
HOMEPAGE = "https://github.com/jmespath/jmespath.py"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2683790f5fabb41a3f75b70558799eb4"

inherit pypi setuptools3

SRC_URI[md5sum] = "37a906c06de62bed25ec5cf99cee04a6"
SRC_URI[sha256sum] = "6a81d4c9aa62caf061cb517b4d9ad1dd300374cd4706997aff9cd6aedd61fc64"

RDEPENDS_${PN} += "\
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-math \
    ${PYTHON_PN}-pprint \
    ${PYTHON_PN}-stringold \
"
