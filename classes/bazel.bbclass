DEPENDS += "bazel-native \
           openjdk-8-native \
          "

BAZEL_OUTPUTBASE_DIR ?= "${WORKDIR}/bazel/output_base"
export BAZEL_ARGS="--output_user_root=${WORKDIR}/bazel/user_root \
                   --output_base=${BAZEL_OUTPUTBASE_DIR} \
                   --bazelrc=${S}/bazelrc \
                  "

export JAVA_HOME="${RECIPE_SYSROOT_NATIVE}/usr/lib/jvm/openjdk-8-native"

def bazel_get_flags(d):
    flags = ""
    for i in d.getVar("CC").split()[1:]:
        flags += "--conlyopt=%s --cxxopt=%s " % (i, i)

    for i in d.getVar("CFLAGS").split():
        if i == "-g":
            continue
        flags += "--conlyopt=%s " % i

    for i in d.getVar("BUILD_CFLAGS").split():
        flags += "--host_conlyopt=%s " % i

    for i in d.getVar("CXXFLAGS").split():
        if i == "-g":
            continue
        flags += "--cxxopt=%s " % i

    for i in d.getVar("BUILD_CXXFLAGS").split():
        flags += "--host_cxxopt=%s " % i

    for i in d.getVar("CPPFLAGS").split():
        if i == "-g":
            continue
        flags += "--conlyopt=%s --cxxopt=%s " % (i, i)

    for i in d.getVar("BUILD_CPPFLAGS").split():
        flags += "--host_conlyopt=%s --host_cxxopt=%s " % (i, i)

    for i in d.getVar("LDFLAGS").split():
        flags += "--linkopt=%s " % i

    for i in d.getVar("BUILD_LDFLAGS").split():
        flags += "--host_linkopt=%s " % i

    for i in d.getVar("TOOLCHAIN_OPTIONS").split():
        flags += "--linkopt=%s " % i

    return flags

bazel_do_configure () {
    cat > "${S}/bazelrc" <<-EOF
build --verbose_failures
build --spawn_strategy=standalone --genrule_strategy=standalone
build --jobs=${@oe.utils.cpu_count()}
test --verbose_failures --verbose_test_summary
test --spawn_strategy=standalone --genrule_strategy=standalone

build --linkopt=-Wl,-latomic
build --strip=never

fetch --distdir=${DL_DIR}
build --distdir=${DL_DIR}

EOF

}

bazel_do_configure_append_class-target () {
    cat >> "${S}/bazelrc" <<-EOF
# FLAGS
build ${@bazel_get_flags(d)}
EOF

    sed -i "s:${WORKDIR}:${BAZEL_OUTPUTBASE_DIR}/external/yocto_compiler:g" ${S}/bazelrc
}

EXPORT_FUNCTIONS do_configure
