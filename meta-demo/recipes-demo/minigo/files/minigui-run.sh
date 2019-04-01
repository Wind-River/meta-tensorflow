#!/bin/bash

set -e

export MINIGO_DIR="/opt/minigo"
export MINIGUI_PYTHON="python3"
export MINIGUI_MODEL="000000-bootstrap"
export MINIGUI_TMPDIR="/opt/minigo/tmp"
export MINIGUI_BOARD_SIZE="19"
export MINIGUI_PORT="5001"
export MINIGUI_HOST="0.0.0.0"
export MINIGUI_CONV_WIDTH="256"
export MINIGUI_NUM_READS="10"

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

{
	cd $SCRIPT_DIR

	echo "Using: the following options for Minigui launch:"
	echo "--------------------------------------------------"
	echo "MINIGUI_PYTHON:      ${MINIGUI_PYTHON}"
	echo "MINIGUI_MODEL:       ${MINIGUI_MODEL}"
	echo "MINIGUI_TMPDIR:      ${MINIGUI_TMPDIR}"
	echo "MINIGUI_BOARD_SIZE:  ${MINIGUI_BOARD_SIZE}"
	echo "MINIGUI_PORT:        ${MINIGUI_PORT}"
	echo "MINIGUI_HOST:        ${MINIGUI_HOST}"
	echo "MINIGUI_CONV_WIDTH:  ${MINIGUI_CONV_WIDTH}"
	echo "MINIGUI_NUM_READS:   ${MINIGUI_NUM_READS}"

	echo
	echo "Finding Model files:"
	echo "--------------------------------------------------"

	model_tmpdir="${MINIGO_DIR}/models"
	control_tmpdir="${MINIGUI_TMPDIR}/control"
	mkdir -p ${control_tmpdir}

	MODEL_SUFFIXES=( "data-00000-of-00001" "index" "meta" )
	for suffix in "${MODEL_SUFFIXES[@]}"
	do
		file_to_check="${model_tmpdir}/${MINIGUI_MODEL}.${suffix}"
		echo "Checking for: ${file_to_check}"
		if [[ ! -f "${file_to_check}" ]]; then
			echo "Can not found model!"
			exit 1
		fi
	done

	# Assume models need to be frozen if .pb doesn't exist.
	cd ..
	model_path="${model_tmpdir}/${MINIGUI_MODEL}"
	if [[ ! -f "${model_path}.pb" ]]; then
		echo
		echo "Freezing model"
		echo "--------------------------------------------------"

		BOARD_SIZE=$MINIGUI_BOARD_SIZE $MINIGUI_PYTHON freeze_graph.py \
			--model_path=${model_path} --conv_width=$MINIGUI_CONV_WIDTH
	fi

	echo
	echo "Running Minigui!"
	echo "--------------------------------------------------"
	echo "Model: ${model_path}"
	echo "Size:  ${MINIGUI_BOARD_SIZE}"

	control_path="${control_tmpdir}/${MINIGUI_MODEL}.ctl"
	cat > ${control_path} << EOL
board_size=19
players = {
  "${MINIGUI_MODEL}" : Player("${MINIGUI_PYTHON}"
                       " -u"
                       " gtp.py"
                       " --load_file=${model_path}"
                       " --minigui_mode=true"
                       " --num_readouts=${MINIGUI_NUM_READS}"
                       " --conv_width=${MINIGUI_CONV_WIDTH}"
                       " --resign_threshold=-0.8"
                       " --verbose=2",
                       startup_gtp_commands=[],
                       environ={"BOARD_SIZE": str(board_size)}),
}
EOL

	${MINIGUI_PYTHON} minigui/serve.py \
		--control="${control_path}" \
		--port "${MINIGUI_PORT}" \
		--host "${MINIGUI_HOST}"
}
