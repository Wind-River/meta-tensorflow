# TensorFlow/TensorFlow Lite Demo on Yocto

## Demo 1. Facial recognition
### 1.1 What the demo does
* Build TensorFlow/TensorFlow Lite for Yocto
* Use Yocto build system to retrain an image classifier model
* Use Yocto build system to retrain a custom image recognition model
* Use Yocto build system to optimize the model for embedded device.
* Run optimized model to do face recognition on embedded device.

### 1.2 Hardware
* A build server to run Yocto build system
* An [Intel NUC](https://www.intel.com/content/www/us/en/products/boards-kits/nuc.html)

>![picture](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/files/nuc.jpg)

* A USB stick

>![picture](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/files/usb_stick.jpg)

* A USB webcam

>![picture](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/files/usb_webcam.jpg)

### 1.3 Build for image collection

```
1) Clone away
$ mkdir <ts-project>
$ cd <ts-project>
$ git clone git://git.yoctoproject.org/meta-tensorflow
$ git clone git://git.openembedded.org/meta-openembedded
$ git clone git://git.yoctoproject.org/meta-yocto
$ git clone git://git.openembedded.org/openembedded-core oe-core
$ cd oe-core
$ git clone git://git.openembedded.org/bitbake

2) Prepare build
$ . <ts-project>/oe-core/oe-init-build-env <ts-build>
$ echo 'MACHINE = "genericx86-64"' >> conf/local.conf
$ echo 'DISTRO_FEATURES_append = " ts-demo x11"' >> conf/local.conf
$ bitbake-layers add-layer <ts-project>/meta-openembedded/meta-python
$ bitbake-layers add-layer <ts-project>/meta-openembedded/meta-oe
$ bitbake-layers add-layer <ts-project>/meta-yocto/meta-yocto-bsp
$ bitbake-layers add-layer <ts-project>/meta-tensorflow
$ bitbake-layers add-layer <ts-project>/meta-tensorflow/meta-demo

3) Build demo image.
$ cd <ts-build>
$ bitbake core-image-x11-ts-demo

4) Copy demo image to USB stick
$ sudo dd if=core-image-x11-ts-demo-genericx86-64.wic of=/dev/sdb bs=1M
2999+1 records in
2999+1 records out
3145043968 bytes (3.1 GB, 2.9 GiB) copied, 205.67 s, 15.3 MB/s
```

### 1.4 Collect labeled face image
```
1) Login target
sh-4.4:~#

2) Collect hongxu's face image
sh-4.4:~# collect_face --label hongxu --dir /root/person --time 30
In 30 seconds, record hongxu 414 images to /root/person/hongxu
```
>![picture](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/files/label_hongxu.gif)
```
3) Upload face images to build server
sh-4.4:~# scp -r /root/person user@build-server:<ts-build>
```

### 1.5 Use labeled images to retrain model
```
1) Set labeled images path
$ cd <ts-build>
$ echo 'RETRAIN_DATASET = "<ts-build>/person"' >> conf/local.conf
$ echo 'SAMPLE_IMAGES = "<ts-build>/person"' >> conf/local.conf

2) Rebuild demo image.
$ bitbake core-image-x11-ts-demo

3) Copy demo image to USB stick
Follow 1.3.4
```

### 1.6 Run optimized model to do face recognition
```
1) Login target
sh-4.4:~#

2) Recognize face and label person name
sh-4.4:~# recognize_face
```
>![picture](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/files/tensorflow-demo.gif)

## Demo 2. [Neural Machine Translation](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/recipes-demo/nmt/README-nmt.md)

## Demo 3. [Minigo: A minimalist Go engine modeled after AlphaGo Zero](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/recipes-demo/minigo/README.md)
