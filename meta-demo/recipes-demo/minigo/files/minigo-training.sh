#! /bin/bash

NEXT_MODEL="000001-first_generation"

python3 train.py \
        outputs/data/selfplay/* \
        --work_dir=estimator_working_dir \
        --export_path=outputs/models/$NEXT_MODEL
