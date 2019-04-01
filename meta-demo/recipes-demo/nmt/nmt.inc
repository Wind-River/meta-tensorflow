SUMMARY = "Neural Machine Translation Based on Tensorflow"
HOMEPAGE = "https://github.com/tensorflow/nmt"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/tensorflow/nmt.git \
          "
SRCREV = "0be864257a76c151eef20ea689755f08bc1faf4e"
S = "${WORKDIR}/git"


DEPENDS += " \
    tensorflow-native \
    python3-native \
    python3-numpy-native \
    python3-protobuf-native \
    python3-absl-native \
    python3-gast-native \
    python3-astor-native \
    python3-termcolor-native \
    tensorflow-estimator-native \
"
RDEPENDS_${PN} += "tensorflow \
                   python3 \
                   python3-numpy \
"
inherit python3native

FILES_${PN} += "${libdir}/*"
