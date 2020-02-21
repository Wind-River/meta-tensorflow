DESCRIPTION = "TensorFlow C/C++ Libraries"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01e86893010a1b87e69a213faa753ebd"

DEPENDS = "bazel-native protobuf-native util-linux-native protobuf"
SRCREV = "c8875cbb1341f6ca14dd0ec908f1dde7d67f7808"
SRC_URI = "git://github.com/tensorflow/tensorflow.git;branch=r1.13 \
           file://0001-SyntaxError-around-async-keyword-on-Python-3.7.patch \
           file://0001-use-local-bazel-to-workaround-bazel-paralle-issue.patch \
           file://0001-grpc-Define-gettid-only-for-glibc-2.30.patch \
           file://0001-fix-compilation-error.patch \
          "
S = "${WORKDIR}/git"

DEPENDS += " \
    python3 \
    python3-numpy-native \
    python3-keras-applications-native \
    python3-keras-preprocessing-native \
    python3-pip-native \
    python3-wheel-native \
"

inherit python3native bazel native

export PYTHON_BIN_PATH="${PYTHON}"
export PYTHON_LIB_PATH="${PYTHON_SITEPACKAGES_DIR}"

do_configure_append () {
    TF_NEED_CUDA=0 \
    TF_NEED_OPENCL_SYCL=0 \
    TF_NEED_OPENCL=0 \
    TF_CUDA_CLANG=0 \
    TF_DOWNLOAD_CLANG=0 \
    TF_ENABLE_XLA=0 \
    TF_NEED_MPI=0 \
    TF_SET_ANDROID_WORKSPACE=0 \
    ./configure
}

do_compile () {
    unset CC
    ${BAZEL} build \
        -c opt \
        --subcommands --explain=${T}/explain.log \
        --verbose_explanations --verbose_failures \
        --verbose_failures \
        //tensorflow/tools/pip_package:build_pip_package

    ${BAZEL} shutdown
}

do_install() {
    export TMPDIR="${WORKDIR}"
    echo "Generating pip package"
    BDIST_OPTS="--universal" \
        ${S}/bazel-bin/tensorflow/tools/pip_package/build_pip_package ${WORKDIR}

    echo "Installing pip package"
    install -d ${D}/${PYTHON_SITEPACKAGES_DIR}
    ${STAGING_BINDIR_NATIVE}/pip3 install --disable-pip-version-check -v --no-deps \
        -t ${D}/${PYTHON_SITEPACKAGES_DIR} --no-cache-dir ${WORKDIR}/tensorflow*.whl

    install -d ${D}${sbindir}
    (
        cd ${D}${PYTHON_SITEPACKAGES_DIR}/bin;
        for app in `ls`; do
            mv $app ${D}${sbindir}
        done

    )

}
