#!/usr/bin/python3
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
import numpy as np
import cv2 as cv
import argparse

from tensorflow_for_poets.label_image_lite import label_image

face_cascade = cv.CascadeClassifier('/usr/share/opencv4/haarcascades/haarcascade_frontalface_default.xml')
eye_cascade = cv.CascadeClassifier('/usr/share/opencv4/haarcascades/haarcascade_eye.xml')

def _face_detection(img):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(img, 1.3, 5)

    i = 0
    for (x,y,w,h) in faces:
        if (x > 50 and y > 50):
            image = "/tmp/face_%d.jpg" % i
            cv.imwrite(image, img[y-50:y+h+50, x:x+w])
            name, score = label_image(image)
            if name:
                text = "{} (score={:0.5f})".format(name, score)
                _putText(img, text, x-50, y-50)
                i+=1

            cv.rectangle(img,(x,y),(x+w,y+h),(255,0,0),2)

def _putText(img, text, pos_x, pos_y):
    for i, line in enumerate(text.split('\n')):
        y = pos_y + (i-1)*20
        cv.putText(frame, line, (pos_x, y), cv.FONT_HERSHEY_SIMPLEX, 0.8, 255)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()

    args = parser.parse_args()

    camera = cv.VideoCapture(0)

    cv.namedWindow('Video',cv.WINDOW_NORMAL)
    cv.resizeWindow('Video', 1024, 768)
    cv.moveWindow('Video', 0, 0)

    while True:
        if cv.waitKey(1) & 0xFF == ord('q'):
            break

        rv, frame = camera.read()
        if rv:
            _face_detection(frame)
            cv.imshow('Video', frame)

    # When everything done, release the capture
    camera.release()
    cv.destroyAllWindows()
