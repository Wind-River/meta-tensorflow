SUMMARY = "Common protobufs used in Google APIs"
HOMEPAGE = "https://github.com/googleapis/googleapis"
DESCRIPTION = "googleapis-common-protos contains the python \
classes generated from the common protos in the googleapis repository."
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2deedbf0e737771f60166811e74a6a92"

inherit pypi

SRC_URI[md5sum] = "0bd91051964266af3bb0e31bc62cc2df"
SRC_URI[sha256sum] = "627ec53fab43d06c1b5c950e217fa9819e169daf753111a7f244e94bf8fb3384"

BBCLASSEXTEND = "native"
