require minigo.inc

SRC_URI += "file://minigo-bootstrap.sh \
            file://minigo-selfplay.sh \
            file://minigo-training.sh \
            file://play-against-minigo.sh \
            file://minigui-run.sh \
           "

RDEPENDS_${PN} = "\
        python3-absl \
        python3-six \
        python3-protobuf \
        python3-numpy \
        python3-grpcio \
        python3-requests \
        python3-tqdm \
        python3-pytz \
        python3-google-auth \
        python3-google-api-core \
        python3-googleapis-common-protos \
        python3-google-cloud-core \
        python3-google-cloud-bigtable \
        python3-cachetools \
        python3-grpc-google-iam-v1 \
        tensorflow \
        tensorflow-estimator \
        bash \
        "

RDEPENDS_${PN}-gui = "\
        minigo \
        python3-flask-socketio \
        "

do_install() {
    install -d ${D}/opt/minigo
    cp -r ${S}/* ${D}/opt/minigo

    install -d ${D}/opt/minigo/scripts
    install -m 0755 ${WORKDIR}/{minigo-bootstrap.sh,minigo-selfplay.sh,minigo-training.sh,play-against-minigo.sh} ${D}/opt/minigo/scripts
    install -m 0755 ${WORKDIR}/minigui-run.sh ${D}/opt/minigo/minigui
}

PACKAGES =+ "${PN}-gui"
FILES_${PN}-gui = "/opt/minigo/minigui/"
FILES_${PN} = "/opt/minigo/"
