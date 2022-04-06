include runc.inc

SRCREV = "f46b6ba2c9314cfc8caae24a32ec5fe9ef1059fe"
SRC_URI = " \
    git://github.com/opencontainers/runc;branch=release-1.0;protocol=https \
    file://0001-Makefile-respect-GOBUILDFLAGS-for-runc-and-remove-re.patch \
    "
RUNC_VERSION = "1.0.3"

CVE_PRODUCT = "runc"
