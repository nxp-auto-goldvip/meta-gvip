# Copyright 2021 NXP
# Uncomment lines in inittab with getty spawn on default serial consoles if Xen non-Dom0less enabled

do_install_append() {
    tmp="${SERIAL_CONSOLES}"
    for i in $tmp; do
        # Parse each item in SERIAL_CONSOLES to obtain the inittab entry ID

        # (e.g. 115200;ttyLF0 => LF0)
        label=`echo ${i} | sed -e 's/tty//' -e 's/^.*;//' -e 's/;.*//' -e 's/.*\(....\)/\1/'`

        # Uncomment each line that contains default serial console device getty spawn
        sed -i "/^#${label}/s/^#//" "${D}${sysconfdir}/inittab"
    done
}
