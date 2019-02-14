DESCRIPTION = "A suite of web applications for inspecting and understanding \
your TensorFlow runs and graphs."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e74df23890b9521cc481e3348863e45d"

SRC_URI = "git://github.com/tensorflow/tensorboard.git; \
           file://0001-customize-for-Yocto.patch \
           file://BUILD \
           file://BUILD.yocto_compiler \
           file://CROSSTOOL.tpl \
           file://yocto_compiler_configure.bzl \
          "
SRCREV = "7194c7486a0c4d107322ffad102c1ca0fcc0fc24"
S = "${WORKDIR}/git"

RDEPENDS_${PN} += "python3 \
           python3-numpy \
           python3-protobuf \
           python3-grpcio \
           python3-werkzeug \
           python3-six \
           python3-markdown \
"
inherit python3native bazel

do_configure_append () {
    mkdir -p ${S}/third_party/toolchains/yocto/
    install -m 644 ${WORKDIR}/BUILD ${S}/third_party/toolchains/yocto/
    install -m 644 ${WORKDIR}/CROSSTOOL.tpl ${S}/third_party/toolchains/yocto/
    install -m 644 ${WORKDIR}/yocto_compiler_configure.bzl ${S}/third_party/toolchains/yocto/
    install -m 644 ${WORKDIR}/BUILD.yocto_compiler ${S}

    CT_NAME=$(echo ${HOST_PREFIX} | rev | cut -c 2- | rev)
    SED_COMMAND="s#%%CT_NAME%%#${CT_NAME}#g"
    SED_COMMAND="${SED_COMMAND}; s#%%WORKDIR%%#${WORKDIR}#g"
    SED_COMMAND="${SED_COMMAND}; s#%%YOCTO_COMPILER_PATH%%#${BAZEL_OUTPUTBASE_DIR}/external/yocto_compiler#g"

    sed -i "${SED_COMMAND}" ${S}/BUILD.yocto_compiler \
                            ${S}/third_party/toolchains/yocto/CROSSTOOL.tpl \
                            ${S}/WORKSPACE
}

do_compile () {
    unset CC
    DESTDIR=${WORKDIR}/python-tensorboard \
     ${STAGING_BINDIR_NATIVE}/bazel run \
        --cpu=armeabi \
        --subcommands --explain=${T}/explain.log \
        --verbose_explanations --verbose_failures \
        --crosstool_top=@local_config_yocto_compiler//:toolchain \
        --verbose_failures \
        //tensorboard/pip_package:build_pip_package

    ${STAGING_BINDIR_NATIVE}/bazel shutdown
}

do_install () {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    cp -rf ${WORKDIR}/python-tensorboard/* ${D}${PYTHON_SITEPACKAGES_DIR}
}

FILES_${PN} += "${libdir}/*"
