SUMMARY = "GRPC library for the google-iam-v1 service"
HOMEPAGE = "https://github.com/googleapis/googleapis"
DESCRIPTION = "grpc-google-iam-v1 is the IDL-derived library \
for the google-iam (v1) service in the googleapis repository."
SECTION = "devel/python"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2deedbf0e737771f60166811e74a6a92"

inherit pypi

SRC_URI[md5sum] = "8ead45e8fc38ade73896b98802c9c84f"
SRC_URI[sha256sum] = "5009e831dcec22f3ff00e89405249d6a838d1449a46ac8224907aa5b0e0b1aec"

BBCLASSEXTEND = "native"
