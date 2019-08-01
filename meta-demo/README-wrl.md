# TensorFlow/TensorFlow Lite Demo on Wind River Linux

## Demo 1. Facial recognition
### 1.1 What the demo does
* Build TensorFlow/TensorFlow Lite for Yocto
* Use Yocto build system to train an image classifier model
* Use Yocto build system retrain a custom image recognition model
* Use Yocto build system optimize your model for embedded device.
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
1) Setup with intel-x86-64 + wrlinux + xfce
$ mkdir <ts-project>
$ cd <ts-project>
$ git clone --branch master-wr --single-branch git://<wrlinux-mirror>/wrlinux-x
$ ./wrlinux-x/setup.sh --machines intel-x86-64 --distro wrlinux \
      --templates feature/xfce --dl-layers

2) Prepare build
$ . <ts-project>/oe-init-build-env <ts-build>
# Allow fetch from internet
$ sed -i "s/BB_NO_NETWORK/#BB_NO_NETWORK/" conf/local.conf

# Disable whitelist mechanism
$ echo 'INHERIT_DISTRO_remove = "whitelist"' >> conf/local.conf

# Install demo face-detection to image and enable distro feature
$ echo 'IMAGE_INSTALL_append = " face-detection"' >> conf/local.conf
$ echo 'DISTRO_FEATURES_append = " ts-demo"' >> conf/local.conf

# Add external layers
$ bitbake-layers add-layer <ts-project>/meta-tensorflow
$ bitbake-layers add-layer <ts-project>/meta-tensorflow/meta-demo

3) Build demo image.
$ cd <ts-build>
$ bitbake  wrlinux-image-glibc-std

4) Copy demo image to USB stick
$ sudo dd if=wrlinux-image-glibc-std-intel-x86-64.wic of=/dev/sdb bs=1M
3102+1 records in
3102+1 records out
3252919296 bytes (3.3 GB, 3.0 GiB) copied, 889.168 s, 3.7 MB/s
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
$ bitbake wrlinux-image-glibc-std

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
