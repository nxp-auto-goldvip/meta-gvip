FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://fix-missing-macro.patch;patchdir=${S}/aws-c-mqtt/"
