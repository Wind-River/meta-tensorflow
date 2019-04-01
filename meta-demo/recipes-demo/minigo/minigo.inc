SUMMARY = "A minimalist Go engine modeled after AlphaGo Zero, built on MuGo"
HOMEPAGE = "https://github.com/tensorflow/minigo"
DESCRIPTION = "An implementation of a neural-network based Go AI, using TensorFlow. \
While inspired by DeepMind's AlphaGo algorithm, this project is not a DeepMind project \
nor is it affiliated with the official AlphaGo project."
SECTION = "demo"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a9923a91d2c553be6c1b23d7d4c731d8"

SRC_URI = "git://github.com/tensorflow/minigo.git;branch=master"
SRCREV = "dde33d5f0176d446f5ab1a2e6a392bb32bf0e720"
PV = "0.0+git${SRCPV}"

S = "${WORKDIR}/git"

