SUMMARY = "Python parser for the CommonMark Markdown spec"
HOMEPAGE = "https://github.com/rtfd/commonmark.py"
AUTHOR = "Bibek Kafle <bkafle@gmail.com>, Roland Shoemaker rolandshoema@gmail.com>"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=37e127eb75a030780aefcfc584e78523"

SRC_URI[md5sum] = "cd1dc70c4714d9ed4117a40490c25e00"
SRC_URI[sha256sum] = "452f9dc859be7f06631ddcb328b6919c67984aca654e5fefb3914d54691aed60"

inherit pypi setuptools3

BBCLASSEXTEND += "native"
