import numpy as np
import cv2

def video2imgs(video_name, size):
   

    img_list = []

    cap = cv2.VideoCapture(video_name)

    while cap.isOpened():
        ret, frame = cap.read()
        if ret:
            gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            img = cv2.resize(gray, size, interpolation=cv2.INTER_AREA)

            img_list.append(img)
        else:
            break

    cap.release()

    return img_list
if __name__ == "__main__":
    assert len(imgs) > 10
pixels = ".`~.-_`~!.~1?COL#&#&%@$"

def img2chars(img):
    res = []

    height, width = img.shape
    for row in range(height):
        line = ""
        for col in range(width):
            percent = img[row][col] / 255
            
            index = int(percent * (len(pixels) - 1))

            line += pixels[index] + " "
        res.append(line)

    return res
def imgs2chars(imgs):
    video_chars = []
    for img in imgs:
        video_chars.append(img2chars(img))

    return video_chars
if __name__ == "__main__":
    imgs = video2imgs("jiangnan.mp4", (64, 48))
    video_chars = imgs2chars(imgs)
    assert len(video_chars) > 10
import time
import subprocess
import curses

def play_video(video_chars):
    width, height = len(video_chars[0][0]), len(video_chars[0])

    stdscr = curses.initscr()
    curses.start_color()
    try:
        stdscr.resize(height, width * 2)

        for pic_i in range(len(video_chars)):
            for line_i in range(height):
                stdscr.addstr(line_i, 0, video_chars[pic_i][line_i], curses.COLOR_WHITE)
            stdscr.refresh()  

            time.sleep(1 / 24)  
    finally:
        curses.endwin()
    return
if __name__ == "__main__":
    imgs = video2imgs("jiangnan.mp4", (64, 48)) #path of video here
    video_chars = imgs2chars(imgs)
    input("Enter to start")
    play_video(video_chars)
