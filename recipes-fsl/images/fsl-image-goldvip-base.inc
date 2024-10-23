#
# Copyright 2022-2024 NXP
#

require recipes-fsl/images/fsl-image-vip-base.inc

# Add packages common between dom0 and other domUs.
IMAGE_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pfe-slave', 'pfe-slave', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'optee', 'optee-examples-goldvip', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-containerization', 'k3s k3s-airgap-pause-container', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'goldvip-crypto', 'opensc p11-kit', '', d)} \
"

