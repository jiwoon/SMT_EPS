# -*- coding: UTF-8 -*-
import socket
import sys  #used to deal with the error
import struct
import types
import re
import time
import os
import threading
import subprocess
import uuid
from Tkinter import *
import RPi.GPIO as GPIO

base = [str(x) for x in range(10)] + [ chr(x) for x in range(ord('A'),ord('A')+6)]

base_zero = [str(x) for x in range(10)] + [ chr(x) for x in range(ord('A'),ord('A')+6)]

def filewrite(list):   #对板子数以及当前状态的写操作
    f = open('/home/pi/Downloads/msg.txt', 'w+')
    f.writelines(list)  # 将list内容逐行写入文件
    # f.flush()
    f.close()

def filewrite_state(list):   #对板子数以及当前状态的写操作
    f = open('/home/pi/Downloads/state.txt', 'w+')
    f.writelines(list)  # 将list内容逐行写入文件
    # f.flush()
    f.close()

def fileread():   #对板子数的读操作
    f = open('/home/pi/Downloads/msg.txt', 'r')

    content = f.readlines()
    f.close()
    return content

def mul_tail_deal(msg): #content内有0d0a的情况的处理
    msg = msg.encode('hex')
    msg = msg.lower()
    print msg
    n = 0
    str_tail_deal = ''
    size = len(msg)
    print size
    msg = msg[4:size-4]
    while n<size:
        if(msg[n:n+2]=='0d' and msg[n+2:n+4]=='0a'):
            str_tail_deal = str_tail_deal + 'ffff' + msg[n:n+2]
        else:
            str_tail_deal = str_tail_deal + msg[n:n+2]
        n = n+2
    # print str_tail_deal
    str_tail_deal = "\x80\x80" + str_tail_deal.decode('hex') + "\x0D\x0A"
    # print str_tail_deal
    return str_tail_deal

def dec2hex_zero(string_num):
    num = int(string_num)
    mid = []
    while True:
        if num == 0: break
        num,rem = divmod(num, 16)
        mid.append(base[rem])

    # return ''.join([str(x) for x in mid[::-1]])
    strn = ''.join([str(x) for x in mid[::-1]])
    print strn
    strn = strn.zfill(4)
    return strn

def dec2hex(string_num):
    num = int(string_num)
    mid = []
    while True:
        if num == 0: break
        num,rem = divmod(num, 16)
        mid.append(base[rem])

    # return ''.join([str(x) for x in mid[::-1]])
    strn = ''.join([str(x) for x in mid[::-1]])
    # print strn
    # strn = strn.zfill(4)
    return strn

i = 0
k = 0
def calculate():
    global i, k
    i = i + 1
    if i > 255:
        i = 0
        k = k+1
        if k > 255:
            k = 0

    crc = chr(k)+chr(i)
    print chr(k)
    return crc

def crc_init(send_crc):
    data = ''
    for i_num in send_crc:
        data_t = str(dec2hex(str(ord(i_num))))
        data_connect = data_t.zfill(2)
        data = data + data_connect
    return data


class Lookup:
    crctab16 = [
        0X0000, 0X1189, 0X2312, 0X329B, 0X4624, 0X57AD, 0X6536, 0X74BF,
        0X8C48, 0X9DC1, 0XAF5A, 0XBED3, 0XCA6C, 0XDBE5, 0XE97E, 0XF8F7,
        0X1081, 0X0108, 0X3393, 0X221A, 0X56A5, 0X472C, 0X75B7, 0X643E,
        0X9CC9, 0X8D40, 0XBFDB, 0XAE52, 0XDAED, 0XCB64, 0XF9FF, 0XE876,
        0X2102, 0X308B, 0X0210, 0X1399, 0X6726, 0X76AF, 0X4434, 0X55BD,
        0XAD4A, 0XBCC3, 0X8E58, 0X9FD1, 0XEB6E, 0XFAE7, 0XC87C, 0XD9F5,
        0X3183, 0X200A, 0X1291, 0X0318, 0X77A7, 0X662E, 0X54B5, 0X453C,
        0XBDCB, 0XAC42, 0X9ED9, 0X8F50, 0XFBEF, 0XEA66, 0XD8FD, 0XC974,
        0X4204, 0X538D, 0X6116, 0X709F, 0X0420, 0X15A9, 0X2732, 0X36BB,
        0XCE4C, 0XDFC5, 0XED5E, 0XFCD7, 0X8868, 0X99E1, 0XAB7A, 0XBAF3,
        0X5285, 0X430C, 0X7197, 0X601E, 0X14A1, 0X0528, 0X37B3, 0X263A,
        0XDECD, 0XCF44, 0XFDDF, 0XEC56, 0X98E9, 0X8960, 0XBBFB, 0XAA72,
        0X6306, 0X728F, 0X4014, 0X519D, 0X2522, 0X34AB, 0X0630, 0X17B9,
        0XEF4E, 0XFEC7, 0XCC5C, 0XDDD5, 0XA96A, 0XB8E3, 0X8A78, 0X9BF1,
        0X7387, 0X620E, 0X5095, 0X411C, 0X35A3, 0X242A, 0X16B1, 0X0738,
        0XFFCF, 0XEE46, 0XDCDD, 0XCD54, 0XB9EB, 0XA862, 0X9AF9, 0X8B70,
        0X8408, 0X9581, 0XA71A, 0XB693, 0XC22C, 0XD3A5, 0XE13E, 0XF0B7,
        0X0840, 0X19C9, 0X2B52, 0X3ADB, 0X4E64, 0X5FED, 0X6D76, 0X7CFF,
        0X9489, 0X8500, 0XB79B, 0XA612, 0XD2AD, 0XC324, 0XF1BF, 0XE036,
        0X18C1, 0X0948, 0X3BD3, 0X2A5A, 0X5EE5, 0X4F6C, 0X7DF7, 0X6C7E,
        0XA50A, 0XB483, 0X8618, 0X9791, 0XE32E, 0XF2A7, 0XC03C, 0XD1B5,
        0X2942, 0X38CB, 0X0A50, 0X1BD9, 0X6F66, 0X7EEF, 0X4C74, 0X5DFD,
        0XB58B, 0XA402, 0X9699, 0X8710, 0XF3AF, 0XE226, 0XD0BD, 0XC134,
        0X39C3, 0X284A, 0X1AD1, 0X0B58, 0X7FE7, 0X6E6E, 0X5CF5, 0X4D7C,
        0XC60C, 0XD785, 0XE51E, 0XF497, 0X8028, 0X91A1, 0XA33A, 0XB2B3,
        0X4A44, 0X5BCD, 0X6956, 0x80DF, 0X0C60, 0X1DE9, 0X2F72, 0X3EFB,
        0XD68D, 0XC704, 0XF59F, 0XE416, 0X90A9, 0X8120, 0XB3BB, 0XA232,
        0X5AC5, 0X4B4C, 0X79D7, 0X685E, 0X1CE1, 0X0D68, 0X3FF3, 0X2E7A,
        0XE70E, 0XF687, 0XC41C, 0XD595, 0XA12A, 0XB0A3, 0X8238, 0X93B1,
        0X6B46, 0X7ACF, 0X4854, 0X59DD, 0X2D62, 0X3CEB, 0X0E70, 0X1FF9,
        0XF78F, 0XE606, 0XD49D, 0XC514, 0XB1AB, 0XA022, 0X92B9, 0X8330,
        0X7BC7, 0X6A4E, 0X58D5, 0X495C, 0X3DE3, 0X2C6A, 0X1EF1, 0X0F78]

    def __init__(self):
        pass

    def getcrc16(self,data, nLength):#长度为包长度到信息序列号的长度
        size = len(data)
        # print size
        n = 0
        x = 0
        num = range(nLength+1)
        while(n<size):
            str = data[n:n+2]
            # print str
            num[x] = int(str, 16)
            # print num[x]
            n = n+2
            x = x+1
        # print num
        fcs = 0xffff
        r = 0
        while nLength > 0:
            fcs = (fcs >> 8) ^ self.crctab16[(fcs ^num[r]) & 0xff]
            nLength = nLength - 1
            r = r + 1
        fcs = ~fcs
        fcs &= 0xFFFF
        fcs = dec2hex_zero(fcs)
        fcs1 = int(fcs[0:2],16)
        fcs2 = int(fcs[2:4],16)
        # print fcs
        return chr(fcs1)+chr(fcs2)

    def checkcrc16(self,data, nLength):   #长度为包长度到信息序列号的长度
        print data
        print nLength
        size = len(data)
        n = 0
        x = 0
        num = range(nLength+1)
        while(n<size):
            str = data[n:n+2]
            # print str
            num[x] = int(str, 16)
            # print num[x]
            n = n+2
            x = x+1
        fcs = 0xffff
        r = 0
        while nLength > 0:
            fcs = (fcs >> 8) ^ self.crctab16[(fcs ^num[r]) & 0xff]
            nLength = nLength - 1
            r = r + 1
        fcs = ~fcs
        fcs &= 0xFFFF
        print fcs
        fcs_str = dec2hex_zero(fcs)
        # fcs1 = int(fcs[0:2],16)
        # fcs2 = int(fcs[2:4],16)
        # fcs = fcs1.zfill(2) + fcs2.zfill(2)

        print fcs_str.lower()
        return fcs_str.lower()          #大写全部转换为小写

def timestamp_datetime(value):
 format = '%Y-%m-%d %H:%M:%S'# value为传入的值为时间戳(整形)，如：1388328820
 value = time.localtime(value)# 经过localtime转换后变成
 ## time.struct_time(tm_year=2012, tm_mon=3, tm_mday=28, tm_hour=6, tm_min=53, tm_sec=40, tm_wday=2, tm_yday=88, tm_isdst=0)
 # 最后再经过strftime函数转换为正常日期格式。
 dt = time.strftime(format, value)
 return dt

str_warning = '0'#报警灯state
str_jiebo = '1' #接驳台state
str_infrare = '1'#红外state
str_state = '00000'+str_warning+str_jiebo+str_infrare
filewrite_state(str_state)
def ctl_solve(str_deal,sock):  #控制的处理
    global str_warning
    global str_jiebo
    global str_infrare
    global str_state
    str_deal = str_deal[0:20]
    print str_deal
    str_crc = str_deal[0:16]

    print str_crc

    print str_deal[16:20]
    search = Lookup()
    if str_deal[16:20]==search.checkcrc16(str_crc, 8):
        print "check succeed"
        if str_deal[8:10]=='01': #01为中控
            if str_deal[10:12]=='00':   #中控关机
                print "poweroff"
                os.system("poweroff")
            elif str_deal[10:12]=='02':
                print "reboot"          #中控重启
                subprocess.call(['reboot'])
        elif str_deal[8:10]=='02':  #02为接驳台
            if str_deal[10:12]=='00':   #关
                GPIO.output(13, 1)
                time.sleep(0.2)
                GPIO.output(13, 0)
                GPIO.output(18, 1)
                
                str_jiebo = '0'
                str_state = '00000'+str_warning+str_jiebo+str_infrare
                filewrite_state(str_state)
            elif str_deal[10:12]=='01': #开
                GPIO.output(16,1)
                time.sleep(0.2)
                GPIO.output(16, 0)
                GPIO.output(18, 0)
                
                str_jiebo = '1'
                str_state = '00000'+str_warning+str_jiebo+str_infrare
                filewrite_state(str_state)
        elif str_deal[8:10]=='03':  #报警灯
            if str_deal[10:12]=='00':   #关
                GPIO.output(15,0)
                str_warning = '0'
                str_state = '00000'+str_warning+str_jiebo+str_infrare
                filewrite_state(str_state)
            elif str_deal[10:12]=='01': #开
                GPIO.output(15,1)
                str_warning = '1'
                str_state = '00000'+str_warning+str_jiebo+str_infrare
                filewrite_state(str_state)
        str_xulie_H = chr(int(str_deal[12:14],16))
        str_xulie_L = chr(int(str_deal[14:16], 16))
        str_xulie = str_xulie_H + str_xulie_L
        str_ctl_falg = chr(int(str_deal[4:6],16))

        ctl_crc_back = "\x08\x43" + str_ctl_falg+ "\x01\x00" + str_xulie
        ctl_crc_check = crc_init(ctl_crc_back)
        ctl_reply = Lookup()
        str_crc_check = ctl_reply.getcrc16(ctl_crc_check, 7)
        ctl_reply_msg = "\x80\x80" + ctl_crc_back + str_crc_check + "\x0D\x0A"
        ctl_reply_msg = mul_tail_deal(ctl_reply_msg)
        print ctl_reply_msg.encode('hex')
        sock.sendall(ctl_reply_msg)
        # calculate()
        # buffer.clear()
    else:
        return

def zeroclear_solve(str_deal,sock):  #终端对清除板子数包命令的处理
    global num
    str_crc = str_deal[0:14]
    # print str_crc
    search = Lookup()
    if str_deal[14:18]==search.checkcrc16(str_crc, 7):
        print '123'
        num = 0
        label_num.config(text=str(num))
        list_zero = [str(num) + '\n']
        filewrite(list_zero)
        str_xulie_H = chr(int(str_deal[10:12],16))
        str_xulie_L = chr(int(str_deal[12:14], 16))
        # str_crc_H = chr(int(str_deal[14:16],16))
        # str_crc_L = chr(int(str_deal[16:18], 16))
        str_xulie = str_xulie_H + str_xulie_L
        server_flag = chr(int(str_deal[4:6],16))
        str_crc_back = "\x08\x52"+server_flag +"\x01\x00" + str_xulie
        str_crc_check = crc_init(str_crc_back)
        bdnum_zero = Lookup()
        str_crc_check = bdnum_zero.getcrc16(str_crc_check, 7)

        # print str_xulie

        msg = "\x80\x80\x08\x52"+server_flag +"\x01\x00" + str_xulie +str_crc_check  + "\x0D\x0A"      #控制结果
        msg = mul_tail_deal(msg)
        print msg.encode('hex')
        sock.sendall(msg)
    else:
        return

def solve_all(str_deal,sock):
    # str_deal = ''.join(str_deal)
    print str_deal
    str_agreement = str_deal[2:4]
    print str_agreement
    if (str_agreement == '43'):  # 43:控制包处理
        print "True2"
        ctl_solve(str_deal,sock)
    elif (str_agreement == '52'):  # 52:板子清零包处理
        print "True3"
        zeroclear_solve(str_deal,sock)
    else:
        return


def link(sock, addr):
    print 'Accept new connection from %s:%s...' % addr
    reply_ahead = ''
    while True:

        reply = sock.recv(1)
        reply = reply.encode('hex')
        #print reply

        data_ahead = ''

        if reply == '80' and reply_ahead == '80':

            # lock.acquire()
            buffer_list = []
            while True:
                data = sock.recv(1)
                data = data.encode('hex')
                if data_ahead == '0d' and data == '0a':
                    buffer_list.append(data)
                    buffer = ''.join(buffer_list)
                    print buffer
                    if buffer[-8:-4] == 'ffff':
                        buffer = buffer[:-8] + buffer[-4:]
                        data_ahead = data
                        buffer_list = []
                        buffer_list = list(buffer)
                        print buffer
                        continue
                    solve_all(buffer,sock)

                    link(sock, addr)

                buffer_list.append(data)

                #print buffer
                data_ahead = data
        reply_ahead = reply



# def thr3(p):
#     while True:
#         print "xxxxx"
#         time.sleep(5)

#-------GPIO control and show num--------
num_content = fileread()
num = int(num_content[0],10)
def num_update():
        global num
        label_num.config(text=str(num))
        while True:
            channel = GPIO.wait_for_edge(29, GPIO.FALLING)
            print channel
            if channel is None:
                num = num
                #GPIO.input(29,1)

            else:
                num = num+1
                list = [str(num) + '\n']
                filewrite(list)
                #print num

            label_num.config(text=str(num))
            time.sleep(0.5)
        #var = time.strftime("%H:%M:%S")

        # 

        # content = fileread()
        # print content[1]
        # i +=1
        # top.after(100, num_update)


GPIO.setmode(GPIO.BOARD)
GPIO.setup(29, GPIO.IN,pull_up_down=GPIO.PUD_UP)
GPIO.setup(13, GPIO.OUT,initial=0) #初始默认为低电平，继电器高电平触发
GPIO.setup(15,GPIO.OUT,initial=0) 
GPIO.setup(16,GPIO.OUT,initial=0) 
GPIO.setup(18,GPIO.OUT,initial=0) 

GPIO.setwarnings(False)


top = Tk()
top.title("Calculate")
w, h = top.maxsize()
top.geometry("{}x{}".format(w,h))
label_text = Label(top,text="NUMBER",font=("Arial",90),width=10)
label_text.pack(anchor=CENTER)
label_num = Label(top, text="", bg="gray", font=("Arial", 90), width=10)
label_num.pack(anchor=CENTER)


# lock = threading.Lock()

# strn = '\x08'
# strn = strn.encode('hex')
# print strn
# strn = chr(int(strn,16))

# print strn.encode('hex')
host = ''
port = 12345
try:
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
except:
    print 'Failed to create socket.'

    sys.exit()
print 'socket created successfully'

s.bind((host, port))
s.listen(64)        #最多64个socket

# m = 100
# n = 800
# list = [str(m)+'\n' , str(n)+'\n']
# filewrite(list)
# content = fileread()
# print content[1]


def thr1(p):
    while True:
        sock,address = s.accept()

        t = threading.Thread(target=link,args=(sock,address))
        t.start()

def thr3(q):

    top.mainloop()
    GPIO.cleanup()

def thr2(r):
    num_update()


threads = []
t1 = threading.Thread(target=thr1,args=('',))
threads.append(t1)
t2 = threading.Thread(target=thr2,args=('',))
threads.append(t2)
# t3 = threading.Thread(target=thr3,args=('',))
# threads.append(t3)


if __name__=='__main__':
    for t in threads:
        t.setDaemon(True)
        t.start()
    #for t in threads:
        #t.join()
    top.mainloop()

