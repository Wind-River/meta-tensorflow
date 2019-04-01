# Neural Machine Translation (seq2seq) Demo

## Description [nmt](https://github.com/tensorflow/nmt)
* vien.bb: For support vi/en translation.  Use Yocto build system
  to train vi-en translation model
* Run translation model to do vi-en translation on embeded device

## Traning Model
* After traning model during build time,  need to install the model
  on the target, current installed in /usr/lib/python3.7/site-packages/
  nmt/vien_model

## Running Model

* python -m nmt.nmt --out_dir=/usr/lib/python3.7/site-packages/nmt/vien_model/ \
  --inference_input_file=./my_infer_file.vi \
  --inference_output_file=./output_infer

* out_dir: should be the dir where you installed the model
