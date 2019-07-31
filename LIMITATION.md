# Limitation
```
* Bazel build takes lots of time, since it like bitbake which has own rules
  and builds everything from scratch. Currently bazel could not reuse Yocto
  DEPENDS/RDEPENDS.

* In order to run tensorflow cases in a reasonable time, although it builds
  successfully on qemuarm, qemuarm64, qemumips, qemumips64, qemux86 and
  qemux86-64, only qemux86-64 with kvm for runtime test.

* It failed to use pre-build model to do predict/inference on big-endian platform
  (such as qemumips), since upstream does not support big-endian very well
  https://github.com/tensorflow/tensorflow/issues/16364

* Do not support 32-bit powerpc (qemuppc) since BoringSSL does not support it.
  (BoringSSL is a fork of OpenSSL used to implement cryptography and TLS across
  most of Google's products)

* If host(build system) is not x86_64, please add meta-java to BBLAYERS in
  conf/bblayers.conf (git://git.yoctoproject.org/meta-java)
```
