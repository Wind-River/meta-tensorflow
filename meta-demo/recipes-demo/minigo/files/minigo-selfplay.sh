#! /bin/bash

MODEL_NAME="000000-bootstrap"
NUM_SELFPLAY=5

i=0
while [ $i -lt $NUM_SELFPLAY ]; do
	python3 selfplay.py \
		--load_file=outputs/models/$MODEL_NAME \
		--num_readouts 10 \
		--verbose 3 \
		--selfplay_dir=outputs/data/selfplay \
		--holdout_dir=outputs/data/holdout \
		--sgf_dir=outputs/sgf
	let i+=1
done
