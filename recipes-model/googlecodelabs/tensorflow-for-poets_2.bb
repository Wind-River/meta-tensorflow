DESCRIPTION = "The code for the 'TensorFlow for poets 2' series of codelabs. \
In this demo,  you could retrain your own image classification model \
"
LICENSE = "Apache-2.0 & CC-BY-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=9f477f13ab4adc51e96f634c8616f710 \
    file://${COMMON_LICENSE_DIR}/CC-BY-2.0;md5=b79db37a058b24d186ed078d34982463 \
"

SRC_URI = "git://github.com/googlecodelabs/tensorflow-for-poets-2.git; \
           file://0001-customize-for-Yocto.patch \
           file://label_image_lite.py \
          "
SRCREV = "faaac68f29086127e1bec0f7d3b3796f0040d796"
S = "${WORKDIR}/git"

# Flowers dataset for retrain which license is CC-BY-2.0
SRC_URI += "http://download.tensorflow.org/example_images/flower_photos.tgz;name=flower"
SRC_URI[flower.md5sum] = "6f87fb78e9cc9ab41eff2015b380011d"
SRC_URI[flower.sha256sum] = "4c54ace7911aaffe13a365c34f650e71dd5bf1be0a58b464e5a7183e3e595d9c"

DEPENDS += " \
    tensorflow-native \
    python3-numpy-native \
    python3-keras-applications-native \
    python3-keras-preprocessing-native \
    python3-protobuf-native \
    python3-grpcio-native \
    python3-absl-native \
    python3-astor-native \
    python3-gast-native \
    python3-termcolor-native \
    tensorflow-estimator-native \
"
RDEPENDS_${PN} += "tensorflow \
                   python3-core \
                   python3-pillow \
"
inherit python3native

export IMAGE_SIZE="224"
export ARCHITECTURE="mobilenet_0.50_${IMAGE_SIZE}"

do_compile[vardeps] += "RETRAIN_DATASET"
do_install[vardeps] += "SAMPLE_IMAGES"

RETRAIN_DATASET ??= "${WORKDIR}/flower_photos"
SAMPLE_IMAGES ??= "${WORKDIR}/flower_photos/daisy/3475870145_685a19116d.jpg"

do_compile () {
    ${PYTHON} -m scripts.retrain \
        --bottleneck_dir=tf_files/bottlenecks \
        --how_many_training_steps=500 \
        --model_dir=tf_files/models/ \
        --summaries_dir=tf_files/training_summaries/"${ARCHITECTURE}" \
        --output_graph=tf_files/retrained_graph.pb \
        --output_labels=tf_files/retrained_labels.txt \
        --architecture="${ARCHITECTURE}" \
        --image_dir="${RETRAIN_DATASET}"

    tflite_convert \
        --graph_def_file=tf_files/retrained_graph.pb \
        --output_file=tf_files/optimized_graph.lite \
        --input_format=TENSORFLOW_GRAPHDEF \
        --output_format=TFLITE \
        --input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
        --input_array=input \
        --output_array=final_result \
        --inference_type=FLOAT \
       --input_data_type=FLOAT
}

do_install () {
    install -d ${D}/${PYTHON_SITEPACKAGES_DIR}/tensorflow_for_poets
    for file in retrain.py label_image.py __init__.py; do
        install -m 755 ${S}/scripts/$file ${D}/${PYTHON_SITEPACKAGES_DIR}/tensorflow_for_poets
    done
    install -m 755 ${WORKDIR}/label_image_lite.py ${D}/${PYTHON_SITEPACKAGES_DIR}/tensorflow_for_poets

    install -d ${D}${datadir}/label_image
    install -m 644 ${S}/tf_files/*.pb ${D}${datadir}/label_image
    install -m 644 ${S}/tf_files/*.txt ${D}${datadir}/label_image
    install -m 644 ${S}/tf_files/*.lite ${D}${datadir}/label_image
    [ -n "${SAMPLE_IMAGES}" ] && [ -e "${SAMPLE_IMAGES}" ] && \
        cp -rf ${SAMPLE_IMAGES} ${D}${datadir}/label_image/
}

FILES_${PN} += "${libdir}/* ${datadir}/*"
