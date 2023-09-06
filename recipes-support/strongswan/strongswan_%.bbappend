# Copyright 2023 NXP

PACKAGECONFIG:append = " stroke"

do_install:append() {
    # Append include directives to ipsec.conf and ipsec.secrets conf files.
    printf "\ninclude /etc/ipsec.*.secrets\n" >> ${D}${sysconfdir}/ipsec.secrets
    printf "\ninclude /etc/ipsec.d/*.conf\n" >> ${D}${sysconfdir}/ipsec.conf
}

