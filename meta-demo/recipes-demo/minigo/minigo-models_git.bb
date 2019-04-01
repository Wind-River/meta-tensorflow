require minigo.inc

inherit python3native

DEPENDS = "python3-absl-native \
           python3-six-native \
           python3-protobuf-native \
           python3-numpy-native \
           python3-grpcio-native \
           python3-requests-native \
           python3-tqdm-native \
           python3-pytz-native \
           python3-google-auth-native \
           python3-google-api-core-native \
           python3-googleapis-common-protos-native \
           python3-google-cloud-core-native \
           python3-google-cloud-bigtable-native \
           python3-cachetools-native \
           python3-grpc-google-iam-v1-native \
           tensorflow-native \
           tensorflow-estimator-native \
           "

ESTIMATOR_DIR = "${WORKDIR}/estimator_working_dir"
MODELS_DIR = "${WORKDIR}/outputs/models"

MODEL_BOOTSTRAP = "000000-bootstrap"
MODEL_FIRSTGEN = "000001-first_generation"

MODEL_SELFPLAY = "${MODEL_BOOTSTRAP}"
MODEL_TRAINING = "${MODEL_FIRSTGEN}"

NUM_SELFPLAY = "5"
NUM_READOUTS = "10"
SELFPLAY_DIR ??= "${WORKDIR}/outputs/data/selfplay"
HOLDOUT_DIR ??= "${WORKDIR}/outputs/data/holdout"
SGF_DIR ??= "${WORKDIR}/outputs/data/sgf"

minigo_bootstrap() {
    cd ${S}; ${PYTHON} bootstrap.py \
        --work_dir=${ESTIMATOR_DIR} \
        --export_path=${MODELS_DIR}/${MODEL_BOOTSTRAP}
}

minigo_selfplay() {
    cd ${S}
    i=0
    while [ $i -lt ${NUM_SELFPLAY} ]; do
        ${PYTHON} selfplay.py \
            --load_file=${MODELS_DIR}/${MODEL_SELFPLAY} \
            --num_readouts ${NUM_READOUTS} \
            --verbose 1 \
            --selfplay_dir=${SELFPLAY_DIR} \
            --holdout_dir=${HOLDOUT_DIR} \
            --sgf_dir=${SGF_DIR}
    let i+=1
    done
}

minigo_training() {
    cd ${S}; ${PYTHON} train.py \
        ${SELFPLAY_DIR}/* \
        --work_dir=${ESTIMATOR_DIR} \
        --export_path=${MODELS_DIR}/${MODEL_TRAINING}
}

do_compile() {
    minigo_bootstrap
    minigo_selfplay
    minigo_training
}

do_install() {
    install -d ${D}/opt/minigo/models
    install -m 0644 ${MODELS_DIR}/* ${D}/opt/minigo/models
}

FILES_${PN} = "/opt/minigo/models"
