# Copyright 2023 NXP

SRC_URI:remove = " \
    https://github.com/downloads/mozilla/rhino/rhino1_7R4.zip \
"

SRC_URI:prepend = " \
    git://github.com/mozilla/rhino;protocol=https;branch=master \
"
SRCREV = "82ffb8f3e09e77e3b1f5782c35b621e7ca742b58"

S = "${WORKDIR}/git"

