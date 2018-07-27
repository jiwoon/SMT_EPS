# -*- coding: UTF-8 -*-
from Tkinter import *
import time
import RPi.GPIO as GPIO
import threading
#import Tkinter as Tk


# def update_timeText():
#         # Get the current time, note you can change the format as you wish
#
#         current = time.strftime("%H:%M:%S")
#
#         # Update the timeText Label box with the current time
#         timeText.configure(text=current)
#
#         # Call the update_timeText() function after 1 second
#         root.after(1000, update_timeText)
#
#
#
# root = tk.Tk()
# root.wm_title("Simple Clock Example")
#
#
# # Create a timeText Label (a text box)
# timeText = tk.Label(root, text="", font=("Helvetica", 150))
# timeText.pack()
# update_timeText()
# root.mainloop()

i = 0
def time_update():
        global i
        if i==0:
                i = i + 1
                top.after(100, time_update)
                return
        channel = GPIO.wait_for_edge(12, GPIO.FALLING)
        var = str(i)
        if channel is None:
                i = i
        else:
                i = i+1
        if i >= 100:
                GPIO.output(13, 1)      #计数到100，高电平触发
                print i
        #var = time.strftime("%H:%M:%S")
        label_num.config(text=var)
        # i +=1
        top.after(100, time_update)
        #time.sleep(0.1)


GPIO.setmode(GPIO.BOARD)
GPIO.setup(12, GPIO.IN)
GPIO.setup(13, GPIO.OUT,initial=0) #初始默认为低电平，继电器高电平触发
GPIO.setup(15,GPIO.OUT,initial=0) #初始默认为低电平，继电器高电平触发

GPIO.setwarnings(False)


top = Tk()
top.title("Calculate")
w, h = top.maxsize()
top.geometry("{}x{}".format(w,h))
label_text = Label(top,text="NUMBER",font=("Arial",90),width=10)
label_text.pack(anchor=CENTER)
label_num = Label(top, text="", bg="gray", font=("Arial", 90), width=10)
label_num.pack(anchor=CENTER)

#def thr1(p):

time_update()

#def thr2(q):
top.mainloop()
#GPIO.cleanup()

#threads = []
#t1 = threading.Thread(target=thr1,args=('',))
#threads.append(t1)
#t2 = threading.Thread(target=thr2,args=('',))
#threads.append(t2)

#if __name__=='__main__':
#    for t in threads:
#        t.setDaemon(True)
#        t.start()
        
#    for t in threads:
#        t.join()

