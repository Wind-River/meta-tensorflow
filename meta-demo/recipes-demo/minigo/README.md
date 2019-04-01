Minigo Demo
==================================================

Minigo is an implementation of a neural-network based Go AI, using TensorFlow.
While inspired by DeepMind's AlphaGo algorithm, this project is not
a DeepMind project nor is it affiliated with the official AlphaGo project.

Git repository: "[MiniGo](https://github.com/tensorflow/minigo)"


------------
## Build Demo Image With Minigo

### Prepare build environment
```shell
$ mkdir <ts-project>
$ cd <ts-project>
$ git clone git://git.yoctoproject.org/meta-tensorflow
$ git clone git://git.yoctoproject.org/meta-java
$ git clone git://git.openembedded.org/meta-openembedded
$ git clone git://git.yoctoproject.org/meta-yocto
$ git clone git://git.openembedded.org/openembedded-core oe-core
$ cd oe-core
$ git clone git://git.openembedded.org/bitbake

$ . <ts-project>/oe-core/oe-init-build-env <ts-build>
$ echo 'MACHINE = "genericx86-64"' >> conf/local.conf
$ echo 'IMAGE_INSTALL_append = " minigo minigo-gui minigo-models"' >> conf/local.conf

$ bitbake add-layer <ts-project>/meta-openembedded/meta-python
$ bitbake add-layer <ts-project>/meta-openembedded/meta-oe
$ bitbake add-layer <ts-project>/meta-java
$ bitbake add-layer <ts-project>/meta-yocto/meta-yocto-bsp
$ bitbake add-layer <ts-project>/meta-tensorflow
$ bitbake add-layer <ts-project>/meta-tensorflow/meta-demo
```

### Build demo image
```shell
$ bitbake core-image-x11-ts-demo
```

### Deploy the image to USB stick
```shell
$ sudo dd if=core-image-x11-ts-demo-genericx86-64.wic of=/dev/sdb bs=1M
```

------------
## Train Model On Target

The following sequence of commands will allow you to do one iteration of
reinforcement learning and produce the models.

The commands are

 - bootstrap: initializes a random model
 - selfplay: plays games with the latest model, producing data used for training
 - train: trains a new model with the selfplay results from the most recent N
   generations.

### Bootstrap

This command initializes your working directory for the trainer and a random
model. This random model is also exported to `--model-save-path` so that
selfplay can immediately start playing with this random model.

If these directories don't exist, bootstrap will create them for you.

```shell
cd /opt/minigo
export MODEL_NAME=000000-bootstrap
python3 bootstrap.py \
  --work_dir=estimator_working_dir \
  --export_path=outputs/models/$MODEL_NAME
```

### Self-play

This command starts self-playing, outputting its raw game data as tf.Examples
as well as in SGF form in the directories.

```shell
cd /opt/minigo
python3 selfplay.py \
  --load_file=outputs/models/$MODEL_NAME \
  --num_readouts 10 \
  --verbose 3 \
  --selfplay_dir=outputs/data/selfplay \
  --holdout_dir=outputs/data/holdout \
  --sgf_dir=outputs/sgf
```

### Training

This command takes a directory of tf.Example files from selfplay and trains a
new model, starting from the latest model weights in the `estimator_working_dir`
parameter.

Run the training job:

```shell
cd /opt/minigo
python3 train.py \
  outputs/data/selfplay/* \
  --work_dir=estimator_working_dir \
  --export_path=outputs/models/000001-first_generation
```


------------
## Train Model With Native Tensorflow

The minigo-model recipe allows you to do the iteration of reinforcement learning 
and produce the models by using tensorflow-native.
```shell
$ bitbake minigo-models
```
Run the above command will do the following things:

 - Initializes a random model named 000000-bootstrap model.
 - Plays games with the bootstrap model, producing data used for training.
 - Trains a new model named 000001-first_generation with the selfplay results
   from the bootstrap model.

You can adjust the settings in minigo-models.bb and repeat the selfplay and
training steps to produce your own model.


------------
## Play Against Minigo

You can play against Minigo with the following commands:
```shell
# Latest model should look like: /path/to/models/000123-something
cd /opt/minigo
LATEST_MODEL=$(ls -d $MINIGO_MODELS/* | tail -1 | cut -f 1 -d '.')
python3 gtp.py --load_file=$LATEST_MODEL --num_readouts=$READOUTS --verbose=3
```

After some loading messages, it will display `GTP engine ready`, at which point
it can receive commands.  GTP cheatsheet:

```
genmove [color]             # Asks the engine to generate a move for a side
play [color] [coordinate]   # Tells the engine that a move should be played for `color` at `coordinate`
showboard                   # Asks the engine to print the board.
```

------------
## Minigo GUI

Minigui is a UI for Minigo that runs in your browser.

- Change the variables you want in /opt/minigo/minigui/minigui-run.sh
   (these are the defaults):

```shell
	export MINIGO_DIR="/opt/minigo"
    export MINIGUI_PYTHON="python3"
    export MINIGUI_MODEL="000000-bootstrap"
    export MINIGUI_TMPDIR="/opt/minigo/tmp"
    export MINIGUI_BOARD_SIZE="19"
    export MINIGUI_PORT="5001"
    export MINIGUI_HOST="0.0.0.0"
    export MINIGUI_CONV_WIDTH="256"
    export MINIGUI_NUM_READS="10"
```

- Run `cd /opt/minigo/minigui; ./minigui-run.sh`

- Open `http://target-ip:5001` in browser on remote client.

- The buttons in the upper right that say 'Human' can be toggled to set which
  color Minigo will play.


### Minigui modes

Minigui has various modes of operation.

#### Study mode

Study mode allows you to load previously recorded SGF games, explore variations
and explore what the engine thinks of each position.

**Minigui's study mode currently requires the C++ Minigo engine.**

#### Vs mode

A mode that plays two models or engines against each other and displays the
variations that each engine is considering in real time.

#### Demo mode

The original demo built for GTC.

#### Lightweight Demo mode

A lightweight demo mode built as experiment for running Minigo on a Raspberry
Pi.

#### Kiosk mode

A self-play only version of the demo mode.


------------
## More Info

See more at [minigo/README.md](https://github.com/tensorflow/minigo/tree/master/README.md)

