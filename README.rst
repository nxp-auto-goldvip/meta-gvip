================
GVIP Yocto Layer
================

This Yocto layer provides support for building NXP Gateway Vehicle Integration 
Platform (GVIP) on top of NXP Auto Linux BSP and other real time packages.

Complete instructions for building a GVIP image are documented in the README.rst
from GVIP manifests repository.

Dependencies
============
This layer depends on the following Yocto layers:

 - meta-alb: https://source.codeaurora.org/external/autobsps32/meta-alb
 - meta-aws: https://github.com/aws

Note: The above layers have other dependencies that are automatically handled by
following the instructions from GVIP manifests readme.

To manually add meta-gvip layer to your build run the following command from
within the build directory::

  bitbake-layers add-layer meta-gvip
