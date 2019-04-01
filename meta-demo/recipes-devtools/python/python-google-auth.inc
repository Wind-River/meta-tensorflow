SUMMARY = "Google Authentication Library"
HOMEPAGE = "https://github.com/googleapis/google-auth-library-python"
DESCRIPTION = "This library simplifies using Google’s various \
server-to-server authentication mechanisms to access Google APIs."
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

inherit pypi

SRC_URI[md5sum] = "0440718247cc4531422e2a8f4f012e8b"
SRC_URI[sha256sum] = "0f7c6a64927d34c1a474da92cfc59e552a5d3b940d3266606c6a28b72888b9e4"

BBCLASSEXTEND = "native"
