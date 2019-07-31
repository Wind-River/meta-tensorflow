# Build and Run
## 1. Clone away
```
$ mkdir <ts-project>
$ cd <ts-project>
$ git clone git://git.yoctoproject.org/meta-tensorflow
$ git clone git://git.openembedded.org/meta-openembedded
$ git clone git://git.openembedded.org/openembedded-core oe-core
$ cd oe-core
$ git clone git://git.openembedded.org/bitbake
```

## 2. Prepare build
```
$ . <ts-project>/oe-core/oe-init-build-env <build>

# Build qemux86-64 which runqemu supports kvm.
$ echo 'MACHINE = "qemux86-64"' >> conf/local.conf

$ echo 'IMAGE_INSTALL_append = " tensorflow"' >> conf/local.conf

Edit conf/bblayers.conf to include other layers
BBLAYERS ?= " \
    <ts-project>/oe-core/meta \
    <ts-project>/meta-openembedded/meta-python \
    <ts-project>/meta-openembedded/meta-oe \
    <ts-project>/meta-tensorflow \
"
```

## 3. Build image
```
cd <build>
$ bitbake core-image-minimal
```

## 4. Start qemu with slrip + kvm + 5GB memory:
```
$ runqemu qemux86-64 core-image-minimal slirp kvm qemuparams="-m 5120"
```

## 5. Verify the install
```
root@qemux86-64:~# python3 -c "import tensorflow as tf; tf.enable_eager_execution(); print(tf.reduce_sum(tf.random_normal([1000, 1000])))"
tf.Tensor(-604.65454, shape=(), dtype=float32)
```

## 6. Run tutorial case
### Refer: https://www.tensorflow.org/tutorials
```
root@qemux86-64:~# cat >code.py <<ENDOF
import tensorflow as tf
mnist = tf.keras.datasets.mnist

(x_train, y_train),(x_test, y_test) = mnist.load_data()
x_train, x_test = x_train / 255.0, x_test / 255.0

model = tf.keras.models.Sequential([
  tf.keras.layers.Flatten(input_shape=(28, 28)),
  tf.keras.layers.Dense(512, activation=tf.nn.relu),
  tf.keras.layers.Dropout(0.2),
  tf.keras.layers.Dense(10, activation=tf.nn.softmax)
])
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

model.fit(x_train, y_train, epochs=5)
model.evaluate(x_test, y_test)
ENDOF

root@qemux86-64:~# python3 ./code.py
Downloading data from https://storage.googleapis.com/tensorflow/tf-keras-datasets/mnist.npz
11493376/11490434 [==============================] - 7s 1us/step
Instructions for updating:
Colocations handled automatically by placer.
Instructions for updating:
Please use `rate` instead of `keep_prob`. Rate should be set to `rate = 1 - keep_prob`.
Epoch 1/5
60000/60000 [==============================] - 27s 449us/sample - loss: 0.2211 - acc: 0.9346
Epoch 2/5
60000/60000 [==============================] - 24s 408us/sample - loss: 0.0969 - acc: 0.9702
Epoch 3/5
60000/60000 [==============================] - 26s 439us/sample - loss: 0.0694 - acc: 0.9780
Epoch 4/5
60000/60000 [==============================] - 23s 390us/sample - loss: 0.0540 - acc: 0.9832
Epoch 5/5
60000/60000 [==============================] - 24s 399us/sample - loss: 0.0447 - acc: 0.9851
10000/10000 [==============================] - 1s 91us/sample - loss: 0.0700 - acc: 0.9782
```
## 7. TensorFlow/TensorFlow Lite C++ Image Recognition Demo
```
root@qemux86-64:~# time label_image
2019-03-06 06:08:51.076028: I tensorflow/examples/label_image/main.cc:251] military uniform (653): 0.834306
2019-03-06 06:08:51.078221: I tensorflow/examples/label_image/main.cc:251] mortarboard (668): 0.0218695
2019-03-06 06:08:51.080054: I tensorflow/examples/label_image/main.cc:251] academic gown (401): 0.010358
2019-03-06 06:08:51.081943: I tensorflow/examples/label_image/main.cc:251] pickelhaube (716): 0.00800814
2019-03-06 06:08:51.083830: I tensorflow/examples/label_image/main.cc:251] bulletproof vest (466): 0.00535084
real    0m 10.50s
user    0m 3.95s
sys 0m 6.46s
root@qemux86-64:~# time label_image.lite
Loaded model /usr/share/label_image/mobilenet_v1_1.0_224_quant.tflite
resolved reporter
invoked
average time: 1064.8 ms
0.780392: 653 military uniform
0.105882: 907 Windsor tie
0.0156863: 458 bow tie
0.0117647: 466 bulletproof vest
0.00784314: 835 suit
real    0m 1.10s
user    0m 1.07s
sys 0m 0.02s
```
