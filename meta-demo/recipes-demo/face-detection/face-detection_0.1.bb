SUMMARY = "A demo of human face recognition"
SECTION = "demo"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://recognize_face.py \
           file://collect_face.py \
"

RDEPENDS_${PN} = " \
    tensorflow-for-poets \
    python3-opencv \
    opencv-apps \
    opencv-samples \
    kernel-module-uvcvideo \
    python3-core \
"

do_install () {
	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/recognize_face.py ${D}${sbindir}/recognize_face
	install -m 0755 ${WORKDIR}/collect_face.py ${D}${sbindir}/collect_face
}
