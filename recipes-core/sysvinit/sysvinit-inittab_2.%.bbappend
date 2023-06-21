# Copyright 2021-2023 NXP

do_install:append() {
    # Uncomment lines in inittab with getty spawn on default serial consoles if Xen non-Dom0less enabled
    tmp="${SERIAL_CONSOLES}"
    for i in $tmp; do
        # Parse each item in SERIAL_CONSOLES to obtain the inittab entry ID

        # (e.g. 115200;ttyLF0 => LF0)
        label=`echo ${i} | sed -e 's/tty//' -e 's/^.*;//' -e 's/;.*//' -e 's/.*\(....\)/\1/'`

        # Uncomment each line that contains default serial console device getty spawn
        sed -i "/^#${label}/s/^#//" "${D}${sysconfdir}/inittab"
    done

    # While there are various means to set the debug level of the console (e.g., using kernel config,
    # passing the log level in bootargs, using the sysctl configuration), altering it just after
    # getty is started offers a good balance between the logs displayed during the boot and the
    # "quietness" while the system is used - set the debug level to KERNEL_WARNING.
    echo "" >> ${D}${sysconfdir}/inittab
    echo "klvl:12345:once:${base_bindir}/dmesg -n 4" >> ${D}${sysconfdir}/inittab
}
