# Copyright 2023 NXP

# Disable the ldd checks to allow builds on Ubuntu 22.04 systems. 
EXTRA_OEMAKE:append = " LDD=:"

