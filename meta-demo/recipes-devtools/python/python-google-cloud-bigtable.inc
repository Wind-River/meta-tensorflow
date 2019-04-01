SUMMARY = "Google Cloud Bigtable API client library"
HOMEPAGE = "https://github.com/googleapis/google-cloud-python"
DESCRIPTION = "Google Cloud Bigtable is Google’s NoSQL Big Data database service. \
It’s the same database that powers many core Google services, \
including Search, Analytics, Maps, and Gmail."
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi

SRC_URI[md5sum] = "5dfda326b1beea64227e41f5c7ceec1b"
SRC_URI[sha256sum] = "bb113894e1322102d4917740be4870798cad76aa4291ff742ada5548988b2223"

BBCLASSEXTEND = "native"
