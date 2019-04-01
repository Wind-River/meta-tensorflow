SUMMARY = "Google Cloud API client core library"
HOMEPAGE = "https://github.com/googleapis/google-cloud-python"
DESCRIPTION = "This library is not meant to stand-alone. \
Instead it defines common helpers (e.g. base Client classes) \
used by all of the google-cloud-* packages."
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi

SRC_URI[md5sum] = "5d05890630eb76cbf53a708f1052002e"
SRC_URI[sha256sum] = "d85b1aaaf3bad9415ad1d8ee5eadce96d7007a82f13ce0a0629a003a11e83f29"

BBCLASSEXTEND = "native"
