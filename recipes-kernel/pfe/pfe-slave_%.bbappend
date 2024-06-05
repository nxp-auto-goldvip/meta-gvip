# Copyright 2024 NXP

KERNEL_MODULE_PROBECONF += "pfeng-slave"
module_conf_pfeng-slave = "options pfeng-slave manage_port_coherency=1"

FILES:${PN} += "${sysconfdir}/modules-load.d/*"

COMPATIBLE_MACHINE:append = "|(s32g274ardb2)"
