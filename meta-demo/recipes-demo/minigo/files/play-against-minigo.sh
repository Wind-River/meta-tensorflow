#! /bin/bash
#
# After some loading messages, it will display GTP engine ready, at which point it can receive commands.
#
# GTP cheatsheet:
# genmove [color]             # Asks the engine to generate a move for a side
# play [color] [coordinate]   # Tells the engine that a move should be played for `color` at `coordinate`
# showboard                   # Asks the engine to print the board.
#

MINIGO_MODELS="outputs/models"
# Latest model should look like: /path/to/models/000123-something
LATEST_MODEL=$(ls -d $MINIGO_MODELS/* | tail -1 | cut -f 1 -d '.')
READOUTS="10"

python3 gtp.py --load_file=$LATEST_MODEL \
	--num_readouts=$READOUTS \
	--verbose=3
