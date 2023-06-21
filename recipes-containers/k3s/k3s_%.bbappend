# Copyright 2022-2023 NXP

inherit update-rc.d

# poky's gatesgarth branch can't be used to build the latest version of k3s, due to the old version
# of golang package. Fortunately, the k3s rekease includes binaries for arm64 architecture that can
# be used.
SRC_URI += " \
    https://github.com/k3s-io/k3s/releases/download/${PV}/k3s-arm64;name=k3s-bin;unpack=0;downloadfilename=k3s-bin \
    https://github.com/k3s-io/k3s/releases/download/${PV}/k3s-airgap-images-arm64.tar.zst;name=k3s-images;unpack=0;downloadfilename=k3s-airgap-images.tar.zst \
    file://k3s-killall.sh \
    file://k3s-agent.sysvinit \
    file://k3s-server.sysvinit \
"

SRC_URI[k3s-bin.sha256sum] = "a56631bee26c65b300094a29d3b38aadac0fa079ca4253c68bd4bebf3d2c714e"
SRC_URI[k3s-bin.md5sum] = "84bfaaeb93102962f386be7eb7237422"
SRC_URI[k3s-images.sha256sum] = "9f62fd7474ed32992322d0d25c215adf5122d98bc5141b5a05f57bb934f30007"
SRC_URI[k3s-images.md5sum] = "44ac110e98a946fcc19201f4c63df39d"

DEPENDS += "skopeo-native"
# Overwrite the package version from the default meta-virtualization recipe.
PV = "v1.21.11+k3s1"

# Put the k3s executables in /usr/bin instead of /usr/local/bin.
BIN_PREFIX = "${exec_prefix}"

# Skip the compilation step defined in the default recipe, since a pre-build binary
# is installed.
do_compile[noexec] = "1"

# Path where the k3s expects the airgap images.
K3S_IMAGES_DIR = "/var/lib/rancher/k3s/agent/images"
# URL for pause-container image used by k3s.
PAUSE_CONTAINER_TAG = "rancher/mirrored-pause:3.5"

# Install the pre-built binary, the pause container and the agent/server services.
do_install:append() {
    install -d ${D}${BIN_PREFIX}/bin
    install -m 755 ${WORKDIR}/k3s-bin ${D}${BIN_PREFIX}/bin/k3s
    install -m 755 ${WORKDIR}/k3s-killall.sh ${D}${BIN_PREFIX}/bin

    install -d ${D}/${K3S_IMAGES_DIR}
    # Add the airgap images archive. Added via package k3s-airgap-images.
    install -m 0644 ${WORKDIR}/k3s-airgap-images.tar.zst ${D}${K3S_IMAGES_DIR}
    # Add the pause-container image (also included in the airgap images archive).
    # This is required to set up the pods / deployments on the k3s cluster; its
    # presence on the rootfs ensures that the cluster is functioning even when
    # there is no working ethernet connection.
    rm -f ${WORKDIR}/pause-container.tar
    skopeo --override-arch arm64 copy --additional-tag="docker.io/${PAUSE_CONTAINER_TAG}" \
	docker://${PAUSE_CONTAINER_TAG} docker-archive:${WORKDIR}/pause-container.tar
    install -m 0644 ${WORKDIR}/pause-container.tar ${D}${K3S_IMAGES_DIR}

    # Add sysvinit services.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/k3s-server.sysvinit ${D}${sysconfdir}/init.d/k3s-server
        install -m 0755 ${WORKDIR}/k3s-agent.sysvinit ${D}${sysconfdir}/init.d/k3s-agent
   fi
}

PACKAGES =+ "${PN}-airgap-images"

INITSCRIPT_PACKAGES = "${PN}-server ${PN}-agent"
INITSCRIPT_NAME:${PN}-server = "k3s-server"
INITSCRIPT_PARAMS:${PN}-server = "defaults 90"
INITSCRIPT_NAME:${PN}-agent = "k3s-agent"
INITSCRIPT_PARAMS:${PN}-agent = "defaults 90"

FILES:${PN} += " \
    ${K3S_IMAGES_DIR}/pause-container.tar \
    ${BIN_PREFIX}/bin/k3s-killall.sh \
"

FILES:${PN}-airgap-images += "${K3S_IMAGES_DIR}/k3s-airgap-images.tar.zst"
FILES:${PN}-agent += "${sysconfdir}/init.d/k3s-agent"
FILES:${PN}-server += "${sysconfdir}/init.d/k3s-server"

