# Copyright 2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = "\
    file://0001-arm-Revert-GUEST_MAGIC_BASE-value.patch \
"

