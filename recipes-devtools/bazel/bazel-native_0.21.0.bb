DESCRIPTION = "Bazel build and test tool"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[md5sum] = "8c8240b178a35c0f3c1bc03017550270"
SRC_URI[sha256sum] = "6ccb831e683179e0cfb351cb11ea297b4db48f9eab987601c038aa0f83037db4"

SRC_URI = "https://github.com/bazelbuild/bazel/releases/download/${PV}/bazel-${PV}-dist.zip"

inherit native

INHIBIT_SYSROOT_STRIP = "1"

DEPENDS = "coreutils-native \
           zip-native \
           openjdk-8-native \
          "

S="${WORKDIR}"

do_compile () {
    export JAVA_HOME="${RECIPE_SYSROOT_NATIVE}/usr/lib/jvm/openjdk-8-native"
    TMPDIR="${TOPDIR}/bazel" \
    VERBOSE=yes \
    EXTRA_BAZEL_ARGS="--distdir=${DL_DIR}" \
    ./compile.sh
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/output/bazel ${D}${bindir}
}
