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
import Queue

base = [str(x) for x in range(10)] + [ chr(x) for x in range(ord('A'),ord('A')+6)]

base_zero = [str(x) for x in range(10)] + [ chr(x) for x in range(ord('A'),ord('A')+6)]

def GetNowTime():
    return time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(time.time()))

time_cnt = 0
ht_flag = 0
bd_flag = 0
#print GetNowTime()
def timer():
    global time_cnt
    global ht_flag
    global bd_flag
    while True:
        time.sleep(1)
        time_cnt = time_cnt+1
        if (time_cnt%180) ==0:
            ht_flag = 1
            q.put(ht_flag)
            print "ht_flag:"+str(ht_flag)
            # print GetNowTime()
        if (time_cnt%1800) ==0:
            bd_flag = 2
            q.put(bd_flag)
            print "bd_flag:"+str(bd_flag)
        if time_cnt ==7200:
            time_cnt = 0
            # print GetNowTime()

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


def fileread():   #对板子数以及当前状态的读操作
    f = open('/home/pi/Downloads/msg.txt', 'r')

    content = f.readlines()
    f.close()
    return content

def fileread_state():
    f = open('/home/pi/Downloads/state.txt', 'r')

    content = f.readlines()
    f.close()
    return content

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

def get_mac_address():
    mac=uuid.UUID(int = uuid.getnode()).hex[-12:]
    mac_adress =  "".join([mac[e:e+2] for e in range(0,11,2)])
    print mac_adress
    n = 0
    mac_return = ""
    print mac_return.encode('hex')
    while n<=12:
        mac_abc = int(mac_adress[n:n+2],16)
        print type(mac_abc)
        n = n+2
    return mac_return



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
    # print chr(k)
    return crc

def crc_init(send_crc):     #crc初始化，转化为字符串
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
        # return chr(fcs1)+chr(fcs2)

def timestamp_datetime(value):
 format = '%Y-%m-%d %H:%M:%S'# value为传入的值为时间戳(整形)，如：1388328820
 value = time.localtime(value)# 经过localtime转换后变成
 ## time.struct_time(tm_year=2012, tm_mon=3, tm_mday=28, tm_hour=6, tm_min=53, tm_sec=40, tm_wday=2, tm_yday=88, tm_isdst=0)
 # 最后再经过strftime函数转换为正常日期格式。
 dt = time.strftime(format, value)
 return dt

def solve_all(str_deal):
    # str_deal = ''.join(str_deal)
    print str_deal
    str_agreement = str_deal[2:4]
    print str_agreement
    if (str_agreement == '4c') or (str_agreement == '4C'):  # 协议号判断，4c:登陆包
        print "True"
        login_solve(str_deal)
    elif (str_agreement == '48'):  # 48:心跳包
        print "True2"
        heart_solve(str_deal)
    elif (str_agreement == '42'):  # 42:上传板子数包
        print "True3"
        boardnum_reply(str_deal)
    else:
        return

# str_line = ''
def login_solve(str_deal):  #服务器回复的登陆包的处理
    # print str_deal
    global  str_line
    str_deal = str_deal[0:22]
    str_crc = str_deal[0:18]

    print str_crc

    # print str_deal[18:22]
    search = Lookup()
    if str_deal[18:22]==search.checkcrc16(str_crc, 9):
        # print str_deal[6:14]   #时间戳
        str_line = chr(int(str_deal[4:6],16))
        time_cuo = str_deal[6:14]
        time_test = int(time_cuo, 16)
        date_time = timestamp_datetime(time_test)
        print date_time
        #os.system("date -s ",date_time)
        # calculate()

    else:
        return

def heart_solve(str_deal):  #服务器回复的心跳包的处理
    print str_deal
    str_crc = str_deal[0:8]
    print str_crc
    search = Lookup()
    if str_deal[8:12] == search.checkcrc16(str_crc, 4):
        print "heart_check"
        # calculate()
    else:
        return

def boardnum_reply(str_deal):  #终端对上传板子数包的回复的处理
    str_crc = str_deal[0:8]
    print str_crc
    search = Lookup()
    if str_deal[8:12]==search.checkcrc16(str_crc, 4):
        print "boardnum check"
        # calculate()
    else:
        return

cnt = 0
def timeout_reply(msg_login):    #终端发送登陆包or heart bag到服务器，5s服务器无返回超时，包重发
    global cnt
    global buffer_list
    s.settimeout(5)

    try:
        buffer_list = []
        reply_ahead = ''
        data_ahead = ''
        
        while True:
            reply = s.recv(1)
            reply = reply.encode('hex')
            # print reply
            if reply=='80' and reply_ahead=='80':
                while True:
                    data = s.recv(1)
                    data = data.encode('hex')
                    # if data_ahead == 'ff' and data == 'ff':

                    if data_ahead=='0d' and data == '0a':
                        buffer_list.append(data)
                        buffer = ''.join(buffer_list)
                        print buffer
                        if buffer[-8:-4]=='ffff':
                            buffer = buffer[:-8] + buffer[-4:]
                            data_ahead =data
                            buffer_list = []
                            buffer_list = list(buffer)
                            print buffer
                            continue
                        solve_all(buffer)
                        # buffer = []
                        return

                    buffer_list.append(data)

                    # print buffer
                    data_ahead = data
            reply_ahead = reply
    except socket.timeout:

        cnt = cnt + 1
        s.sendall(msg_login)
        if cnt > 3:  # 超时3次，
            time.sleep(5)
            subprocess.call(['reboot'])
            cnt = 0
            return
        print "Timeout heartbeats"
        timeout_reply(msg_login)

# count2 = 0
# def timeout_reply2(msg_heart):    #终端发送心跳包到服务器，5s服务器无返回超时，包重发
#     global count2
#     global buffer_list
#     s1.settimeout(5)
#
#     try:
#         buffer_list = []
#         reply_ahead = ''
#         data_ahead = ''
#         # mutex.acquire()
#         while True:
#             reply = s1.recv(1)
#             reply = reply.encode('hex')
#             # print reply
#             if reply=='80' and reply_ahead=='80':
#                 while True:
#                     data = s.recv(1)
#                     data = data.encode('hex')
#                     if data_ahead=='0d' and data == '0a':
#                         buffer = ''.join(buffer)
#                         solve_all(buffer)
#                         buffer = []
#                         return
#
#                     buffer.append(data)
#
#                     # print buffer
#                     data_ahead = data
#             reply_ahead = reply
#     except socket.timeout:
#
#         count2 = count2 + 1
#         s.sendall(msg_heart)
#         if count2 > 3:  # 超时3次，
#             time.sleep(5)
#             # subprocess.call(['reboot'])
#             count2 = 0
#             return
#         print "Timeout heartbeats2"
#         timeout_reply2(msg_heart)


count = 0
buff_list = []
def timeout_boardnum_reply(msg_bdnum):
    global count
    global buff_list
    s.settimeout(5)

    try:
        buff_list = []
        reply_ahead_brd = ''
        data_ahead_brd = ''
        # mutex.acquire()
        while True:
            reply_brd = s.recv(1)
            reply_brd = reply_brd.encode('hex')
            print reply_brd
            if reply_brd=='80' and reply_ahead_brd=='80':
                while True:
                    data_brd = s.recv(1)
                    data_brd = data_brd.encode('hex')
                    if data_ahead_brd=='0d' and data_brd == '0a':
                        buff_list.append(data_brd)
                        buff = ''.join(buff_list)

                        if buff[-8:-4]=='ffff':
                            buff = buff[:-8] + buff[-4:]
                            data_ahead_brd =data_brd
                            buff_list = []
                            buff_list = list(buff)
                            print buff
                            continue
                        print "slove_all ready!"
                        solve_all(buff)
                        # buff = []
                        return

                    buff_list.append(data_brd)

                    # print buff
                    data_ahead_brd = data_brd
            reply_ahead_brd = reply_brd
    except socket.timeout:
        count = count + 1
        s.sendall(msg_bdnum)
        if count > 3:  # 超时3次，
            time.sleep(5)
            subprocess.call(['reboot'])
            count = 0
            return
        print "Timeout boardnum"
    
    timeout_boardnum_reply(msg_bdnum)

def thr1(m):            #线程1：心跳包发送，周期为3min
    global str_line
    # while True:
    strn = calculate()
    now = int(time.time())
    now16 = dec2hex(now)
    # print now16
    m = 0

    now1 = int(now16[0:2],16)
    now2 = int(now16[2:4],16)
    now3 = int(now16[4:6], 16)
    now4 = int(now16[6:8], 16)
    nowm = chr(now1)+chr(now2)+chr(now3)+chr(now4)

    current_state = fileread_state()
    if len(current_state) == 0:
        time.sleep(0.1)
    state = current_state[0]
    # state_H = chr(int(state[0:4],16))
    state_L = int(state[0:8], 2)
    # print state_L
    # print "-------------------"
    state_add = chr(state_L)
    # print state_add.encode('hex')
    heart_crc = "\x09\x48"+ str_line + nowm+ state_add + strn
    heart_crcn = crc_init(heart_crc)
    # print heart_crcn
    heart = Lookup()
    heart_crc_deal = heart.getcrc16(heart_crcn, 10)
    # print heart_crc_deal.encode('hex')
    msg = "\x80\x80"+heart_crc+heart_crc_deal+"\x0D\x0A"
    msg = mul_tail_deal(msg)
    # print msg.encode('hex')
    try:
        s.sendall(msg)

        timeout_reply(msg)
        print 'send successfully'
    except:
        print 'SEND FAILED'
        # sys.exit()
        # print get_mac()
        # time.sleep(8)

def thr3(p):
    timer()

def thr4(p):
    global bd_flag
    global ht_flag
    while True:
        flag = q.get()
        if flag == 1:
            thr1(1)
            ht_flag = 0
        else:
            pass
        if flag == 2:
            thr2(1)
            bd_flag = 0
        else:
            pass
        time.sleep(0.1)


def thr2(p):        #上传板子数目到服务器
    global str_line
    # while True:
    strn = calculate()
    now = int(time.time())
    now16 = dec2hex(now)
    # print now16
    m = 0
    # bdnum = "\x00\x03\xE8"
    # str_line = "\x08"
    num_content = fileread()    #读出txt文件内板子的数量，存入
    if len(num_content)==0:
        time.sleep(0.1)
    bd_num_read = num_content[0]

    board_num = str(int(bd_num_read, 10))
    str_bdnum = dec2hex(board_num)

    str_bdnum = str_bdnum.zfill(6)
    print str_bdnum
    str_bdnum1 = chr(int(str_bdnum[0:2],16))
    str_bdnum2 = chr(int(str_bdnum[2:4],16))
    str_bdnum3 = chr(int(str_bdnum[4:6],16))
    str_bdnum = str_bdnum1 + str_bdnum2 + str_bdnum3
    bdnum = str_bdnum
    print "+++++++++++++++++"
    now1 = int(now16[0:2],16)
    now2 = int(now16[2:4],16)
    now3 = int(now16[4:6], 16)
    now4 = int(now16[6:8], 16)
    nowm = chr(now1)+chr(now2)+chr(now3)+chr(now4)
    str_crc = "\x0D\x42" + str_line + nowm + bdnum + strn
    board_crc = crc_init(str_crc) #转换
    boardnum = Lookup()
    bd_crc_deal = boardnum.getcrc16(board_crc,12) #crc校验返回
    msg = "\x80\x80\x0D\x42"+ str_line+ nowm + bdnum + strn + bd_crc_deal+"\x0D\x0A"
    msg = mul_tail_deal(msg)
    print msg.encode('hex')
    try:
        s.sendall(msg)
        print 'send_all board'
        timeout_boardnum_reply(msg)
        print 'send_board'
    except:
        print '2 SEND FAILED'
        # sys.exit()
        # print get_mac()
        # finally:
            # time.sleep(8)


try:
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

except:
    print 'Failed to create socket.'

    sys.exit()
print 'socket created successfully'
#
#
host = '192.168.2.9'

port = 23333
# host = '192.168.155.1'
# port = 12345


try:
    des_ip = socket.gethostbyname(host)
   #des_ip ='10.214.145.248'
except socket.gaierror:
    print 'Failed to get host ip,exit'
    sys.exit()
print 'host is:'+host+'  ip:'+des_ip
try:
    s.connect((des_ip,port))


    print 'Connected successfully'
    str_login = calculate()

    mac = "\xB8\x27\xEB\x99\x38\xD6"  # mac地址
    # print str_login
    login_crc = "\x0B\x4C" + mac + str_login

    login_crc = crc_init(login_crc)  # 转换

    login = Lookup()


    print login_crc
    login_crc_deal = login.getcrc16(login_crc, 10)  # crc校验返回


    print login_crc_deal.encode('hex')

    msg_login = "\x80\x80\x0B\x4C" + mac + str_login + login_crc_deal + "\x0D\x0A"
    msg_login = mul_tail_deal(msg_login)
    print msg_login.encode('hex')


    n = 0
    s.sendall(msg_login)
    timeout_reply(msg_login)

except:
    print "connected error"

thr1(1)
thr2(1)
q = Queue.Queue(5)


# mac = get_mac_address()
# print mac
threads = []
#t1 = threading.Thread(target=thr1,args=('',))
#threads.append(t1)
# t2 = threading.Thread(target=thr2,args=('',))
# threads.append(t2)
t3 = threading.Thread(target=thr3,args=('',))
threads.append(t3)
t4 = threading.Thread(target=thr4,args=('',))
threads.append(t4)

if __name__=='__main__':
    for t in threads:
        t.setDaemon(True)
        t.start()
    for t in threads:
        t.join()

