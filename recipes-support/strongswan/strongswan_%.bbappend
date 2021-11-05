
do_install_append() {
    # Append include directives to ipsec.conf and ipsec.secrets conf files.
    echo "\ninclude /etc/ipsec.*.secrets" >> ${D}${sysconfdir}/ipsec.secrets
    echo "\ninclude /etc/ipsec.d/*.conf" >> ${D}${sysconfdir}/ipsec.conf
}

