===================
GoldVIP Yocto Layer
===================

This Yocto layer provides support for building NXP Gateway Vehicle Integration 
Platform (GoldVIP) on top of NXP Auto Linux BSP and other real time packages.

Complete instructions for building a GoldVIP image are documented in the README.rst
from GoldVIP manifests repository.

Dependencies
============
This layer depends on the following Yocto layers:

 - meta-alb: https://source.codeaurora.org/external/autobsps32/meta-alb
 - meta-aws: https://github.com/meta-aws
 - meta-java: https://git.yoctoproject.org/cgit/cgit.cgi/meta-java

Note: The above layers have other dependencies that are automatically handled by
following the instructions from GoldVIP manifests readme.

To manually add meta-gvip layer to your build run the following command from
within the build directory::

  bitbake-layers add-layer meta-gvip
