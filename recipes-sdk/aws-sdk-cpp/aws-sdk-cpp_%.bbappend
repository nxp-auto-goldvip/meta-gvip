# Copyright 2023 NXP

# Avoid building all the aws-sdk-cpp components to save some
# build time and space on the image. Only these two are required
# for the AWS IoT FleetWise Edge Agent.
AWS_SDK_CPP_BUILD_ONLY ?= "-DBUILD_ONLY='s3-crt;iot'"

EXTRA_OECMAKE:append = " ${AWS_SDK_CPP_BUILD_ONLY}"

