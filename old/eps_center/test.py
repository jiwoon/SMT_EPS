from Tkinter import *
import time
import RPi.GPIO as GPIO

i = 0
def time_update():
        global i
        channel = GPIO.wait_for_edge(12, GPIO.FALLING)
        var = str(i)
        if channel is None:
                i = i
        else:
                i = i+1
        #var = time.strftime("%H:%M:%S")
        label_num.config(text=var)
        # i +=1
        top.after(1000, time_update)

GPIO.setmode(GPIO.BOARD)
GPIO.setup(12, GPIO.IN)
GPIO.setwarnings(False)


top = Tk()
top.title("Calculate")
w,h = top.maxsize()
top.geometry("{}x{}".format(w,h))
label_text = Label(top,text="NUMBER",font=("Arial",90),width=10)
label_text.pack(anchor=CENTER)
label_num = Label(top, text="", bg="gray", font=("Arial", 90), width=10)
label_num.pack(anchor=CENTER)
top.mainloop()
time_update()

GPIO.cleanup()




