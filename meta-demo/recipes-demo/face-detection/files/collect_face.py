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
import os
import time
import argparse
import shutil

import numpy as np
import cv2 as cv


face_cascade = cv.CascadeClassifier('/usr/share/opencv4/haarcascades/haarcascade_frontalface_default.xml')
eye_cascade = cv.CascadeClassifier('/usr/share/opencv4/haarcascades/haarcascade_eye.xml')

def _putText(frame, text, pos_x, pos_y):
    for i, line in enumerate(text.split('\n')):
        y = pos_y + (i-1)*20
        cv.putText(frame, line, (pos_x, y), cv.FONT_HERSHEY_SIMPLEX, 0.8, 255)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-l", "--label", default="test", help="name of image to be labeled")
    parser.add_argument("-d", "--dir", default="./person", help="root dir to save labeled image")
    parser.add_argument("-t", "--time", default=10.0, help="Record time (second)")

    args = parser.parse_args()

    cv.namedWindow('Video',cv.WINDOW_NORMAL)
    cv.resizeWindow('Video', 1024, 768)
    cv.moveWindow('Video', 0, 0)

    camera = cv.VideoCapture(0)
    dirname = os.path.join(args.dir, args.label)
    if os.path.exists(dirname):
        shutil.rmtree(dirname)
    os.makedirs(dirname)

    i = 0
    image = ""
    start_time = time.time()
    while True:
        now = time.time()
        if (cv.waitKey(1) & 0xFF == ord('q')) or (start_time + float(args.time) < now):
            print("In %d seconds, record %s %d images to %s" %
                   (int(args.time), args.label, i, dirname))
            break

        rv, frame = camera.read()
        if rv:
            gray = cv.cvtColor(frame, cv.COLOR_BGR2GRAY)
            faces = face_cascade.detectMultiScale(frame, 1.3, 5)
            for (x,y,w,h) in faces:
                if (x > 50 and y > 50):
                    image = "%s/face_%d.jpg" % (dirname, i)
                    cv.imwrite(image, frame[y-50:y+h+50, x:x+w])
                    cv.rectangle(frame,(x,y),(x+w,y+h),(255,0,0),2)

                    text = "Recording %s" % args.label
                    _putText(frame, text, x-50, y-50)
                    i += 1
                # Record first recognize face
                continue
            cv.imshow('Video', frame)

    # When everything done, release the capture
    camera.release()
    cv.destroyAllWindows()
