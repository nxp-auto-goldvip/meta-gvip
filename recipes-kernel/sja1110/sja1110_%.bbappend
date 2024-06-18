# Copyright 2024 NXP

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = " \
	file://0001-sja1110-add-power-management-resume-handler.patch \
"
