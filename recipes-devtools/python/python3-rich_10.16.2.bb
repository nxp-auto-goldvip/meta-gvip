SUMMARY = "Render rich text, tables, progress bars, syntax highlighting, markdown and more to the terminal"
HOMEPAGE = "https://github.com/willmcgugan/rich"
AUTHOR = "Will McGugan <willmcgugan@gmail.com>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b5f0b94fbc94f5ad9ae4efcf8a778303"

SRC_URI[sha256sum] = "720974689960e06c2efdb54327f8bf0cdbdf4eae4ad73b6c94213cad405c371b"

RDEPENDS:${PN} = "\
	python3-colorama \
	python3-commonmark \
	python3-pygments \
	python3-typing-extensions \
"

inherit pypi setuptools3

BBCLASSEXTEND += "native"
