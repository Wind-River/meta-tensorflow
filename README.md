# meta-tensorflow

## Overview
```
TensorFlow is an open source software library for high performance numerical
computation primarily used in machine learning. Its flexible architecture
allows easy deployment of computation across a variety of types of platforms
(CPUs, GPUs, TPUs), and a range of systems from single desktops to clusters
of servers to mobile and edge devices.
(https://www.tensorflow.org/)

The build system of TensorFlow is Bazel (https://bazel.build/).

This layer integrates TensorFlow to OE/Yocto platform
- Integrate Google's bazel to Yocto
- Add Yocto toolchain for bazel to support cross compiling.
- Replace python package system(pip/wheel) with Yocto package system(rpm/deb/ipk).
```

## Prerequisite(s)
### 1. Based on Yocto
Yocto layer dependencies
```
URI: git://github.com/openembedded/openembedded-core.git
branch: master
revision: HEAD

URI: git://github.com/openembedded/bitbake.git
branch: master
revision: HEAD

URI: git://github.com/openembedded/meta-openembedded.git
layers: meta-python, meta-oe
branch: master
revision: HEAD
```
### 2. Based on Wind River Linux
Wind River Linux (CI/CD branch)

## Installation
[Build and Run](https://github.com/Wind-River/meta-tensorflow/blob/master/BUILD.md)

## Demo 1. Facial recognition
### 1.1 Based on Yocto
[Face recognition on Yocto](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/README.md)

### 1.2 Based on Wind River Linux
[Face recognition on Wind River Linux](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/README-wrl.md)
>![picture](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/files/tensorflow-demo.gif)

## Demo 2. [Neural Machine Translation](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/recipes-demo/nmt/README-nmt.md)

## Demo 3. [Minigo: A minimalist Go engine modeled after AlphaGo Zero](https://github.com/Wind-River/meta-tensorflow/blob/master/meta-demo/recipes-demo/minigo/README.md)

## Project License
```
Copyright (C) 2019 Wind River Systems, Inc.

All metadata is MIT licensed unless otherwise stated. Source code included
in tree for individual recipes is under the LICENSE stated in each recipe
(.bb file) unless otherwise stated.
```

## Legal Notices
```
If product is based on Wind River Linux:

All product names, logos, and brands are property of their respective owners.
All company, product and service names used in this software are for identification
purposes only. Wind River are registered trademarks of Wind River Systems.

Disclaimer of Warranty / No Support: Wind River does not provide support and
maintenance services for this software, under Wind River’s standard Software
Support and Maintenance Agreement or otherwise. Unless required by applicable
law, Wind River provides the software (and each contributor provides its
contribution) on an “AS IS” BASIS, WITHOUT WARRANTIES OF ANY KIND, either express
or implied, including, without limitation, any warranties of TITLE, NONINFRINGEMENT,
MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible
for determining the appropriateness of using or redistributing the software
and assume ay risks associated with your exercise of permissions under the license.
```

## Attribution
TensorFlow, the TensorFlow logo and any related marks are trademarks of Google Inc.
