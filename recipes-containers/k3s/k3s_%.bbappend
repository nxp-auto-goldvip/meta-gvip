# Copyright 2022-2024 NXP

inherit update-rc.d

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    https://github.com/k3s-io/k3s/releases/download/${@d.getVar("PV").split("+git")[0]}/k3s-airgap-images-arm64.tar.zst;name=k3s-images;unpack=0;downloadfilename=k3s-airgap-images.tar.zst \
    file://k3s-killall.sh \
    file://k3s-agent.sysvinit \
    file://k3s-server.sysvinit \
"

SRC_URI[k3s-images.sha256sum] = "2332d50a57cacd2c3e4794491d76c99e8dd21be56d691b999db12fa9cdf320bd"
SRC_URI[k3s-images.md5sum] = "43864527a697136584ef3051a048ad78"

DEPENDS += "skopeo-native"

# Put the k3s executables in /usr/bin instead of /usr/local/bin.
BIN_PREFIX = "${exec_prefix}"

# Path where the k3s expects the airgap images.
K3S_IMAGES_DIR = "/var/lib/rancher/k3s/agent/images"
# URL for pause-container image used by k3s.
PAUSE_CONTAINER_TAG = "rancher/mirrored-pause:3.6"

addtask fetch_pause_container after do_configure before do_install
do_fetch_pause_container[network] = "1"

do_fetch_pause_container() {
    rm -f ${WORKDIR}/pause-container.tar
    skopeo --override-arch arm64 copy --additional-tag="docker.io/${PAUSE_CONTAINER_TAG}" \
        docker://${PAUSE_CONTAINER_TAG} docker-archive:${WORKDIR}/pause-container.tar
}

# Install the pause container and the agent/server services.
do_install:append() {
    install -m 755 ${WORKDIR}/k3s-killall.sh ${D}${BIN_PREFIX}/bin

    install -d ${D}/${K3S_IMAGES_DIR}
    # Add the airgap images archive. Added via package k3s-airgap-images.
    install -m 0644 ${WORKDIR}/k3s-airgap-images.tar.zst ${D}${K3S_IMAGES_DIR}
    # Add the pause-container image (also included in the airgap images archive).
    # This is required to set up the pods / deployments on the k3s cluster; its
    # presence on the rootfs ensures that the cluster is functioning even when
    # there is no working ethernet connection.
    install -m 0644 ${WORKDIR}/pause-container.tar ${D}${K3S_IMAGES_DIR}

    # Add sysvinit services.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/k3s-server.sysvinit ${D}${sysconfdir}/init.d/k3s-server
        install -m 0755 ${WORKDIR}/k3s-agent.sysvinit ${D}${sysconfdir}/init.d/k3s-agent
   fi
}

PACKAGES =+ "${PN}-airgap-images ${PN}-airgap-pause-container"

INITSCRIPT_PACKAGES = "${PN}-server ${PN}-agent"
INITSCRIPT_NAME:${PN}-server = "k3s-server"
INITSCRIPT_PARAMS:${PN}-server = "defaults 90"
INITSCRIPT_NAME:${PN}-agent = "k3s-agent"
INITSCRIPT_PARAMS:${PN}-agent = "defaults 90"

FILES:${PN} += " \
    ${BIN_PREFIX}/bin/k3s-killall.sh \
"

FILES:${PN}-agent += " \
    ${sysconfdir}/init.d/k3s-agent \
"

FILES:${PN}-server += " \
    ${sysconfdir}/init.d/k3s-server \
"

FILES:${PN}-airgap-pause-container = " \
    ${K3S_IMAGES_DIR}/pause-container.tar \
"

FILES:${PN}-airgap-images = " \
    ${K3S_IMAGES_DIR}/k3s-airgap-images.tar.zst \
"

