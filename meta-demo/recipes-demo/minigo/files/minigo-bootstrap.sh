#! /bin/bash

MODEL_NAME="000000-bootstrap"

python3 bootstrap.py \
        --work_dir=estimator_working_dir \
        --export_path=outputs/models/$MODEL_NAME
