#!/usr/bin/python3
import numpy as np
import cv2 as cv
import argparse

from tensorflow_for_poets.label_image_lite import label_image

face_cascade = cv.CascadeClassifier('/usr/share/OpenCV/haarcascades/haarcascade_frontalface_default.xml')
eye_cascade = cv.CascadeClassifier('/usr/share/OpenCV/haarcascades/haarcascade_eye.xml')

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
    parser.add_argument("-s", "--save", help="Save avi video", action="store_true")

    args = parser.parse_args()

    camera = cv.VideoCapture(0)

    if args.save:
        # Define the codec and create VideoWriter object
        fourcc = cv.VideoWriter_fourcc(*'XVID')
        frame_width = int(camera.get(3))
        frame_height = int(camera.get(4))
        # Define the codec and create VideoWriter object.The output is stored in 'outpy.avi' file.
        out = cv.VideoWriter('ts-demo.avi', cv.VideoWriter_fourcc('M','J','P','G'),
                              10, (frame_width,frame_height))

    cv.namedWindow('Video',cv.WINDOW_NORMAL)
    cv.resizeWindow('Video', 1024, 768)
    cv.moveWindow('Video', 0, 0)

    while True:
        if cv.waitKey(1) & 0xFF == ord('q'):
            break

        rv, frame = camera.read()
        if rv:
            _face_detection(frame)
            if args.save:
                out.write(frame)
            cv.imshow('Video', frame)

    # When everything done, release the capture
    camera.release()
    if args.save:
        out.release()
    cv.destroyAllWindows()
