require nmt.inc

SRC_URI_append = "file://download_iwslt15.sh \
          "
do_compile(){
    mkdir -p ${S}/vien/nmt_data
    mkdir -p ${S}/vien/nmt_model

    ${WORKDIR}/download_iwslt15.sh ${S}/vien/nmt_data

    ${PYTHON} -m nmt.nmt \
    --src=vi --tgt=en \
    --vocab_prefix=./vien/nmt_data/vocab \
    --train_prefix=./vien/nmt_data/train \
    --dev_prefix=./vien/nmt_data/tst2012 \
    --test_prefix=./vien/nmt_data/tst2013 \
    --out_dir=./vien/nmt_model \
    --num_train_steps=12000 \
    --steps_per_stats=100 \
    --num_layers=2 \
    --num_units=128 \
    --dropout=0.2 \
    --metrics=bleu
}

do_install(){
    install -d ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt
    install -d ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt/vien_model
    install -d ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt/vien_data

    cp -rf ${S}/nmt/* ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt/
    cp -rf ${S}/vien/nmt_model/*  ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt/vien_model/
    cp -rf ${S}/vien/nmt_data/*  ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt/vien_data/

    sed -i -e "s;./vien/nmt_data;${PYTHON_SITEPACKAGES_DIR}/nmt/vien_data;g" \
           -e "s;./vien/nmt_model;${PYTHON_SITEPACKAGES_DIR}/nmt/vien_model;g" \
           ${D}/${PYTHON_SITEPACKAGES_DIR}/nmt/vien_model/hparams
}
