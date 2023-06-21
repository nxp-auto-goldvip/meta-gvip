# Poor man's RTC using timestamp. Install a cronjob to save periodically the current (and
# hopefully synchronized) time and date of the system in the /etc/timestamp file. The timestamp
# saved in the aforementioned file is used to set the system time and date after a reboot.
do_install:append() {
    echo '*/10  * * * * root /etc/init.d/save-rtc.sh' >> ${D}/${sysconfdir}/crontab
}
