SUMMARY = "RTI Connector for Python"
HOMEPAGE = "https://github.com/rticommunity/rticonnextdds-connector-py"
AUTHOR = "RTI <support@rti.com>"
LICENSE = "RTI"
LIC_FILES_CHKSUM = "file://LICENSE.pdf;md5=09abe1594d89513f74f80f3ec746a0b4"

SRC_URI = " \
    https://files.pythonhosted.org/packages/d4/d9/a5bfb95b0aef1eb9c7a900ade75879c106a46eb39018e2d11c95b336e3d1/rticonnextdds_connector-${PV}.tar.gz \
"

SRC_URI[md5sum] = "ad14e113465aa457f49dfb4dc2cf0cd1"
SRC_URI[sha256sum] = "66331cd3d672ceda261ea987b6bafed3a29dc661c5bfad98f8a87cb889f74135"

S = "${WORKDIR}/rticonnextdds_connector-${PV}"

inherit setuptools3

do_install:append:aarch64() {
    # The pypi archive packages shared libraries for multiple platforms, which causes
    # problems in do_package and do_populate_sysroot steps. Keep only the shared libraries
    # meant for the current architecture. One other option would have  been to update the
    # FILES:${PN} variable, but must account for all the other files that must be included
    # in the package (setuptools3 class includes a glob on ${PYTHON_SITEPACKAGES_DIR} which
    # contains some additional directories, i.e. egg-info dir).
    for dir in $(ls -d ${D}${PYTHON_SITEPACKAGES_DIR}/rticonnextdds-connector/lib/*/); do
        if [ "$(basename "$dir")" != "linux-arm64" ]; then
            rm -rf "$dir"
        fi
    done
}

