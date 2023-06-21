# Copyright 2023 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Fix the build on Ubuntu 22.04 systems.
SRC_URI += "file://0001-build-omit-source-symlink-when-building-hypervisor-i.patch;patch=1"

