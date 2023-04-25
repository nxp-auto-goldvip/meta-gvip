#
# Copyright 2023 NXP
#

SRC_URI_remove = " \
    https://github.com/downloads/mozilla/rhino/rhino1_7R4.zip \
"

SRC_URI_prepend = " \
    git://github.com/mozilla/rhino;protocol=https;tag=Rhino1_7R4_RELEASE \
"

S = "${WORKDIR}/git"

