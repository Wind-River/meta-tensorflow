#!/bin/sh
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
