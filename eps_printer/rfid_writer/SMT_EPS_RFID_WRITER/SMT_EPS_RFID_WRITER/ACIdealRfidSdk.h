#ifndef ACIDEAL_RFID_SDK_H_INCLUDED
#define ACIDEAL_RFID_SDK_H_INCLUDED


#define ACIDEAL_RFID_API  __declspec(dllexport) 

//return 0  means ok!
ACIDEAL_RFID_API int ACIDEAL_RFID_Init();
ACIDEAL_RFID_API int ACIDEAL_RFID_GetSN(char * pUniqueId);
ACIDEAL_RFID_API int ACIDEAL_RFID_ReadEPC(char* epc); 
ACIDEAL_RFID_API int ACIDEAL_RFID_WriteEPC(char* epc, int length);
ACIDEAL_RFID_API int ACIDEAL_RFID_Beep_Shine_On();
ACIDEAL_RFID_API int ACIDEAL_RFID_Beep_Shine_Off();
ACIDEAL_RFID_API int SoftReset();//once call this function sdk should be init again

#endif  