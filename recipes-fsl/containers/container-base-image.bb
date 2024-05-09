#
# Copyright 2022 NXP
# Based on meta-virtualization/recipes-extended/images/container-base.bb
#
SUMMARY = "Base GoldVIP Application Container Image"
LICENSE = "LA_OPT_NXP_Software_License"
LIC_FILES_CHKSUM = "file://${GOLDVIP_SOFTWARE_LICENSE};md5=1239b5ec13378bbe9b56958556340101"

OCI_IMAGE_WORKINGDIR ?= "/home/root"
OCI_IMAGE_ENV_VARS ?= " \
  PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin \
"

IMAGE_FSTYPES = "container oci"
inherit image
inherit goldvip-image-oci

IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""
NO_RECOMMENDATIONS = "1"

IMAGE_INSTALL = " \
    base-files \
    base-passwd \
    coreutils \
    netbase \
"

# Allow build with or without a specific kernel.
IMAGE_CONTAINER_NO_DUMMY = "1"

# Workaround /var/volatile.
ROOTFS_POSTPROCESS_COMMAND += "rootfs_fixup_var_volatile ; "
rootfs_fixup_var_volatile () {
    install -m 1777 -d ${IMAGE_ROOTFS}/${localstatedir}/volatile/tmp
    install -m 755 -d ${IMAGE_ROOTFS}/${localstatedir}/volatile/log
}

# Fix do_package warning when depending on this recipe
deltask do_packagedata

