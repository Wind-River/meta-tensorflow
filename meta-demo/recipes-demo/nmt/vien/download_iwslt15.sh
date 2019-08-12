#!/bin/sh
# Copyright (c) 2019, Wind River Systems, Inc.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is 
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
# Download small-scale IWSLT15 Vietnames to English translation data for NMT
# model training.
#
# Usage:
#   ./download_iwslt15.sh path-to-output-dir
#
# If output directory is not specified, "./iwslt15" will be used as the default
# output directory.
OUT_DIR="${1:-iwslt15}"
SITE_PREFIX="https://nlp.stanford.edu/projects/nmt/data"

mkdir -v -p $OUT_DIR

# Download iwslt15 small dataset from standford website.
echo "Download training dataset train.en and train.vi."
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/train.en
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/train.vi

echo "Download dev dataset tst2012.en and tst2012.vi."
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/tst2012.en
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/tst2012.vi

echo "Download test dataset tst2013.en and tst2013.vi."
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/tst2013.en
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/tst2013.vi

echo "Download vocab file vocab.en and vocab.vi."
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/vocab.en
wget -P $OUT_DIR $SITE_PREFIX/iwslt15.en-vi/vocab.vi
