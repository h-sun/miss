/**
 * nativeemul.c
 * Native method handling for unix_impl (emulation).
 */
#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "types.h"
#include "trace.h"
#include "constants.h"
#include "specialsignatures.h"
#include "specialclasses.h"
#include "stack.h"
#include "memory.h"
#include "threads.h"
#include "classes.h"
#include "language.h"
#include "configure.h"
#include "interpreter.h"
#include "exceptions.h"
#include "platform_config.h"
#include "sensors.h"
#include "poll.h"
#include "tvmemul.h"//TODO
#include "syscom.h"
#include "fileoperation.h"
#include "wireless.h"
#include "debug.h"//TODO:nxt version


/*#define MAJOR_VERSION 0
#define MINOR_VERSION 6*/
//#define VERSION_NUMBER 0x000901

/*
Very important!!!
>10: lego car
2:tracked car
3:tricycle
4:quadricycle
others: error number!!
*/
#define VERSION_NUMBER 0x2

/*
Very important!!!
if it is running in old car, please use #define OLD_CAR,
else it is running in new car, pleas use #define NEW_CAR,
WARNING: DON'T USE "#define OLD_CAR" AND "#define NEW_CAR" AT THE SAME TIME
*/
//#define OLD_CAR
//#define NEW_CAR

#undef push_word
#undef push_ref
#define push_word( a) push_word_cur( a)
#define push_ref( a)  push_ref_cur( a)

static TWOBYTES gSensorValue = 0;

static char* sensorReadTypes[3] = {
  "RAW",
  "CANONICAL",
  "BOOLEAN"
};

static char *sensorSetTypes[5] = {
  "RAW",
  "TOUCH",
  "TEMP",
  "LIGHT",
  "ROT"
};

#define DISPLAY_WIDTH 100
#define DISPLAY_DEPTH 8

//TODO @04/26/2012
static struct
{
  BigArray arrayHdr;
  //TODO @04/26/2012 Object hdr;
  unsigned char display[DISPLAY_DEPTH+1][DISPLAY_WIDTH];
} __attribute__((packed)) display_array;

static char display_init()
{
  // Initialise the array parameters so that the display can
  // be memory mapped into the Java address space
//  display_array.hdr.flags.arrays.isArray = 1;
  // NOTE This object must always be marked, otherwise very, very bad
  // things will happen!
//  display_array.hdr.flags.arrays.mark = 1;
//  display_array.hdr.flags.arrays.length = 200;
//  display_array.hdr.flags.arrays.isAllocated = 1;
//  display_array.hdr.flags.arrays.type = T_INT;
//  display_array.hdr.monitorCount = 0;
//  display_array.hdr.threadId = 0;
  display_array.arrayHdr.hdr.flags.mark = 3;
  display_array.arrayHdr.hdr.flags.length = LEN_BIGARRAY;
  display_array.arrayHdr.hdr.flags.class = T_BYTE + ALJAVA_LANG_OBJECT;
  display_array.arrayHdr.hdr.sync.monitorCount = 0;
  display_array.arrayHdr.hdr.sync.threadId = 0;
  display_array.arrayHdr.length = DISPLAY_DEPTH*DISPLAY_WIDTH;
  display_array.arrayHdr.offset = 2;
}

static char *getSensorMode(byte code)
{
  static char smBuffer[256];

  strcpy(smBuffer, "mode = ");
  switch (code & 0xF0)
  {
    case 0x00: strcat(smBuffer, "RAW"); break;
    case 0x20: strcat(smBuffer, "BOOLEAN"); break;
    case 0x40: strcat(smBuffer, "EDGE"); break;
    case 0x60: strcat(smBuffer, "PULSE"); break;
    case 0x80: strcat(smBuffer, "PERCENT"); break;
    case 0xA0: strcat(smBuffer, "DEGC"); break;
    case 0xC0: strcat(smBuffer, "DEGF"); break;
    case 0xE0: strcat(smBuffer, "ANGLE"); break;
    default: sprintf(smBuffer, "mode = INVALID (0x%1X)", code & 0xF0); break;
  }

  sprintf(&smBuffer[strlen(smBuffer)], ", slope = %d", code & 0x0F);
  return smBuffer;
}

extern int	verbose;	/* If non-zero, generates verbose output. */
extern byte *region;

char *get_meaning(STACKWORD *);

/*void dump_flag(Object *obj)
{
  if (is_allocated(obj))
  {
    if (is_gc(obj))
    {
      printf("Ready for the garbage\n");
    }
    else if (is_array(obj))
    {
      printf("Array, type=%d, length=%d\n", get_element_type(obj), get_array_length(obj));
    }
    else
    {
      printf("Class index = %d\n", get_na_class_index(obj));
    }
  }
  else
  {
    printf ("Free block, length=%d\n", get_free_length(obj));
  }

  /**
   * Object/block flags.
   * Free block:
   *  -- bits 0-14: Size of free block in words.
   *  -- bit 15   : Zero (not allocated).
   * Objects:
   *  -- bits 0-7 : Class index.
   *  -- bits 8-12: Unused.
   *  -- bit 13   : Garbage collection mark.
   *  -- bit 14   : Zero (not an array).
   *  -- bit 15   : One (allocated).
   * Arrays:
   *  -- bits 0-8 : Array length (0-527).
   *  -- bits 9-12: Element type.
   *  -- bit 13   : Garbage collection mark.
   *  -- bit 14   : One (is an array).
   *  -- bit 15   : One (allocated).
   */

//}
char* string2chp(String* s)
{
  char *ret = "null";
  if (s->characters)
  {
    int i;
    Object *obj;
    JCHAR *pA;
    int length;
    obj = word2obj(get_word_4_ns((byte*)(&(s->characters))));
    pA = jchar_array(obj);
    length = get_array_length(obj);
    ret = malloc(length+1);
    for (i=0; i<length; i++)
    {
      ret[i] = pA[i];
    }
    ret[i] = 0;
  }

  return ret;
}

/*boolean debug_uncaught_exception(Object * exception,
                          const Thread * thread,
                          const MethodRecord * methodRecord,
                          const MethodRecord * rootMethod,
                          byte * pc, int exceptionFrame)
{
  return false;
}*/

/**
 * NOTE: The technique is not the same as that used in TinyVM.
 */
int dispatch_native (TWOBYTES signature, STACKWORD *paramBase)
{
	ClassRecord	*classRecord;

    switch (signature)
    {
    case wait_4_5V:
      return monitor_wait((Object*) word2ptr(paramBase[0]), 0);
    case wait_4J_5V:
      return monitor_wait((Object*) word2ptr(paramBase[0]), paramBase[2]);
    case notify_4_5V:
      return monitor_notify((Object*) word2ptr(paramBase[0]), false);
    case notifyAll_4_5V:
      return monitor_notify((Object*) word2ptr(paramBase[0]), true);
    case start_4_5V:
      return init_thread ((Thread *) word2ptr(paramBase[0]));
    case yield_4_5V:
      schedule_request( REQUEST_SWITCH_THREAD);
      break;
    case sleep_4J_5V:
      //sleep_thread (paramBase[1]);
      sleep_thread(((int)paramBase[0] > 0 ? 0x7fffffff : paramBase[1]));//TODO:nxt version
      schedule_request( REQUEST_SWITCH_THREAD);
      break;
    case getPriority_4_5I:
      push_word (get_thread_priority ((Thread*)word2ptr(paramBase[0])));
      break;
    case setPriority_4I_5V:
      {
        STACKWORD p = (STACKWORD)paramBase[1];
        if (p > MAX_PRIORITY || p < MIN_PRIORITY)
          //throw_exception(JAVA_LANG_ILLEGALARGUMENTEXCEPTION);
          //return throw_exception(JAVA_LANG_ILLEGALARGUMENTEXCEPTION);//TODO: add return(nxt version)
            break;
        else
          set_thread_priority ((Thread*)word2ptr(paramBase[0]), p);
      }
      break;
    case currentThread_4_5Ljava_3lang_3Thread_2:
      push_ref(ptr2ref(currentThread));
      break;
    case interrupt_4_5V:
      interrupt_thread((Thread*)word2ptr(paramBase[0]));
      break;
    case interrupted_4_5Z:
      {
      	JBYTE i = currentThread->interruptState != INTERRUPT_CLEARED;
      	currentThread->interruptState = INTERRUPT_CLEARED;
      	push_word(i);
      }
      break;
    case isInterrupted_4_5Z:
      push_word(((Thread*)word2ptr(paramBase[0]))->interruptState
                != INTERRUPT_CLEARED);
      break;
    case setDaemon_4Z_5V:
      ((Thread*)word2ptr(paramBase[0]))->daemon = (JBYTE)paramBase[1];
      break;
    case isDaemon_4_5Z:
      push_word(((Thread*)word2ptr(paramBase[0]))->daemon);
      break;
    case join_4_5V:
      join_thread((Thread*)word2ptr(paramBase[0]),0);
      break;
    case join_4J_5V:
      join_thread((Thread*)word2obj(paramBase[0]),paramBase[2]);
      break;
    case halt_4I_5V://TODO: nxt version
      schedule_request(REQUEST_EXIT);
      break;
    case shutdown_4_5V://TODO:nxt version
      shutdown_program(false);
      break;
    case exitThread_4_5V:
      //schedule_request(REQUEST_EXIT);
      currentThread->state = DEAD;//
      schedule_request(REQUEST_SWITCH_THREAD);//TODO:nxt version
      break;
    case currentTimeMillis_4_5J:
      push_word (0);
      push_word (get_sys_time());
      break;
    /*TODO
    case setPoller_4_5V:
      set_poller(word2ptr(paramBase[0]));
      return;*/
    case readSensorValue_4I_5I:
      // Parameters: int portId
 //     if (verbose)
 //        printf("> ");
 //     else
 //        printf("& ");
      //printf("Reading sensor %d, returned value %d\n",paramBase[0], sensors[paramBase[0]].value);
      //TODO: deprecated
      //push_word (uc_sensor_get_value(paramBase[0]));
      break;
    /*TODO
    case setADTypeById_4II_5V:
      if (verbose)
         printf("> ");
      else
         printf("& ");
      printf("Setting sensor %d to AD type %d\n",paramBase[0], paramBase[1]);
      return;*/
    case setPowerTypeById_4II_5V:
 //      if (verbose)
 //        printf("> ");
 //     else
 //        printf("& ");
      printf("Setting sensor %d to power type %d\n",(int)paramBase[0], (int)paramBase[1]);
    break;
    case freeMemory_4_5J:
      push_word (0);
      push_word (getHeapFree());
      break;
    case totalMemory_4_5J:
      push_word (0);
      push_word (getHeapSize());
      break;
    case createStackTrace_4Ljava_3lang_3Thread_2Ljava_3lang_3Object_2_5_1I://TODO:nxt version
    {
      Object *trace = create_stack_trace((Thread *)ref2obj(paramBase[0]), ref2obj(paramBase[1]));
      if (trace == NULL) return EXEC_RETRY;
      push_word(obj2ref(trace));
    }
    break;
    /*TODO
    case test_4Ljava_3lang_3String_2Z_5V:
      if (!paramBase[1])
      {
        printf("%s\n",string2chp((String*)word2ptr(paramBase[0])));
        throw_exception(error);
      }
      return;
    case testEQ_4Ljava_3lang_3String_2II_5V:
      if (paramBase[1] != paramBase[2])
      {
        printf("%s: expected %ld, got %ld\n",string2chp((String*)word2ptr(paramBase[0])), paramBase[1], paramBase[2]);
        throw_exception(error);
      }
      return;*/
    case floatToRawIntBits_4F_5I: // Fall through
    case intBitsToFloat_4I_5F:
      push_word (paramBase[0]);
      break;
    case doubleToRawLongBits_4D_5J:	// Fall through
    case longBitsToDouble_4J_5D:
      push_word(paramBase[0]);
      push_word(paramBase[1]);
      break;
    case drawString_4Ljava_3lang_3String_2II_5V:
      {
        char* str = string2chp((String*)word2ptr(paramBase[0]));
        printf("%s",str);
        /*
        byte *p = word2ptr(paramBase[0]);
        int len, i;
        //printf("displayString: pointer is %x\n",p);
        //printf("Object size is %d\n",sizeof(Object));
        Object *charArray = (Object *) word2ptr(get_word_4_ns(p + HEADER_SIZE));
        //printf("Chars is %x\n",charArray);
        len = charArray->flags.length;
        //printf("length is %d\n",len);
        {
         char buff[len+1];
         char *chars = ((char *) charArray) + HEADER_SIZE;
         //printf("chars is %x\n",chars);
         for(i=0;i<len;i++) buff[i] = chars[i+i];
         buff[len] = 0;
         //if (verbose)
           //printf("> ");
         //else
           //printf("& ");
         printf("%s",buff);
         //printf("drawString called with parameters %s, %d, %d\n",buff,paramBase[1],paramBase[2]);
        }
        */
      }
      break;
    case drawInt_4III_5V:
 //     if (verbose)
 //        printf("> ");
 //     else
 //        printf("& ");
      printf("drawInt called with parameters %d, %d, %d\n",(int)paramBase[0],(int)paramBase[1],(int)paramBase[2]);
      break;
    case drawInt_4IIII_5V:
//      if (verbose)
//         printf("> ");
//      else
//        printf("& ");
      printf("drawInt called with parameters %d, %d, %d, %d\n",(int)paramBase[0],(int)paramBase[1],(int)paramBase[2],(int)paramBase[3]);
      break;
    case asyncRefresh_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Displayed Refreshed\n");
      break;
    case clear_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Display cleared\n");
      break;
    /*TODO
    case setDisplay_4_1I_5V:
      if (verbose)
         printf("> ");
      else
         printf("& ");
      printf("Display set\n");
      return;*/
    case getDisplay_4_5_1B:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
//      printf("Get display\n"); //系统运行时，这里会被调用
      display_init();
      push_word((STACKWORD) ptr2word(&display_array));
      break;
    /*
    *TODO
    */
    case setAutoRefreshPeriod_4I_5I:
      push_word(paramBase[0]);
      break;
    case getRefreshCompleteTime_4_5I:
      push_word(paramBase[0]);
      break;
    /*case setAutoRefresh_4I_5V:
      if (verbose)
         printf("> ");
      else
         printf("& ");
      printf("Set autodisplay to %d\n", paramBase[0]);
      break;*/
    case bitBlt_4_1BIIII_1BIIIIIII_5V:
      {
        Object *src = word2ptr(paramBase[0]);
        Object *dst = word2ptr(paramBase[5]);
//        if (verbose)
//          printf("> ");
//        else
//          printf("& ");
        printf("bitBlt called\n");
        break;
      }
    case getSystemFont_4_5_1B:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
//      printf("getSystemFont called\n"); //系统运行时，这里首先被调用
      push_word(0);
      break;
    /*TODO:
    case getVoltageMilliVolt_4_5I:
      if (verbose)
         printf("> ");
      else
         printf("& ");
      printf("getVoltageMillivolts returning 9999\n");
      push_word(9999);
      return;*/
    case getButtons_4_5I:
      if (verbose)
      {
         printf("> ");
         printf("readButtons returning 0\n");
      }
      push_word(0);
      break;
    case getTachoCountById_4I_5I:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      //printf("getTachoCount on Motor %d returning 0\n", paramBase[0]);
      push_word(uc_motor_get_count(paramBase[0]));
      break;
    case controlMotorById_4III_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      //printf("controlMotor called with parameters %d, %d, %d\n",paramBase[0],paramBase[1],paramBase[2]);
      uc_motor_set_speed(paramBase[0], paramBase[1], paramBase[2]);
      break;
    case resetTachoCountById_4I_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      //printf("resetTachoCount on Motor %d \n", paramBase[0]);
      uc_motor_reset_count(paramBase[0]);
      break;
    case i2cEnableById_4II_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("i2cEnableById\n");
      break;
    case i2cDisableById_4I_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("i2cDisableById\n");
      break;
    /*TODO:
    case i2cBusyById_4I_5I:
      if (verbose)
         printf("> ");
      else
         printf("& ");
      printf("i2cBusyById\n");
      push_word(0);
      return;*/
    case i2cStartById_4II_1BIII_5I:
      {
    	Object *p = word2ptr(paramBase[2]);
    	byte *byteArray = (((byte *) p) + HEADER_SIZE);
        //byte *byteArray = (((byte *) p) + HEADER_SIZE);
//       if (verbose)
//         printf("> ");
//      else
//         printf("& ");

     /*
      printf("i2cStart called with parameters %d, %d, %d %x, %d, %d\n",
                                        paramBase[0],
    	                                paramBase[1],
    	                                byteArray,
    	                                paramBase[4],
    	                                paramBase[5]);
      */
      }
      push_word(0);
      break;
    case playFreq_4III_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("playFreq with freq = %d, duration = %d, volume = %d\n", (int)paramBase[0], (int)paramBase[1], (int)paramBase[2]);
      break;
    case btSend_4_1BI_5V:
      {
        Object *p = word2ptr(paramBase[0]);
        byte *byteArray = (((byte *) p) + HEADER_SIZE);
//        if (verbose)
//          printf("> ");
//        else
//          printf("& ");
          printf("btSend called with parameters %s, %d\n", byteArray, (int)paramBase[1]);
      }
      break;
    case btReceive_4_1B_5V:
      {
        Object *p = word2ptr(paramBase[0]);
        byte *byteArray = (((byte *) p) + HEADER_SIZE);
//        if (verbose)
//          printf("> ");
//        else
//          printf("& ");
        printf("btReceive called with parameter %s\n", byteArray);
      }
      break;
    case btGetBC4CmdMode_4_5I:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
         printf("btGetBC4CmdMode returning 1\n");
      push_word(1);
      return;
    case btSetArmCmdMode_4I_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("btSetArmCmdMode\n");
      break;
    /*TODO:
    case btStartADConverter_4_5V:
      if (verbose)
         printf("> ");
      else
         printf("& ");
      printf("btStartAdConverter\n");
      return;*/
    case btSetResetLow_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("btSetResetLow\n");
      break;
    case btSetResetHigh_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("btSetResetHigh\n");
    break;
    case btWrite_4_1BII_5I:
      {
        Object *p = word2ptr(paramBase[0]);
        byte *byteArray = (((byte *) p) + HEADER_SIZE);
//        if (verbose)
//          printf("> ");
//        else
//          printf("& ");
        printf("btWrite called with parameters %s, %d, %d\n", byteArray,(int)paramBase[1],(int)paramBase[2]);
      }
      break;
    case btRead_4_1BII_5I:
      {
        Object *p = word2ptr(paramBase[0]);
        byte *byteArray = (((byte *) p) + HEADER_SIZE);
//        if (verbose)
//          printf("> ");
//        else
//          printf("& ");
        printf("btRead called with parameters %s, %d, %d\n", byteArray,(int)paramBase[1],(int)paramBase[2]);
      }
      break;
    case btPending_4_5I:
      if (verbose)
      {
//        printf("> ");
        printf("btPending called\n");
      }
      push_word(0);
      break;
    case usbRead_4_1BII_5I:
      {
        Object *p = word2ptr(paramBase[0]);
        byte *byteArray = (((byte *) p) + HEADER_SIZE);
        if (verbose)
        {
//          printf("> ");
          printf("usbReceive called with parameters %s, %d\n", byteArray, (int)paramBase[1]);
        }
        push_word(0);
      }
      break;
    case usbWrite_4_1BII_5I:
      {
        Object *p = word2ptr(paramBase[0]);
        byte *byteArray = (((byte *) p) + HEADER_SIZE);
        if (verbose)
        {
//          printf("> ");
          printf("usbWrite called with parameters %s, %d\n", byteArray, (int)paramBase[1]);
        }
      }
      break;
    case usbStatus_4_5I:
      {
        push_word(0);
      }
      break;
    case usbEnable_4I_5V:
      {
        if (verbose)
        {
//          printf("> ");
          printf("usbEnable called\n");;
        }
      }
      break;
    case usbDisable_4_5V:
      {
        if (verbose)
        {
//          printf("> ");
          printf("usbDisable called\n");;
        }
      }
      break;
    case usbReset_4_5V :
      if (verbose)
      {
//        printf("> ");
        printf("udpReset called\n");
      }
      break;
    case usbSetSerialNo_4Ljava_3lang_3String_2_5V:
      {
        byte *p = word2ptr(paramBase[0]);
        int len;
        Object *charArray = (Object *) word2ptr(get_word_4_ns(fields_start(p)));

        len = get_array_length(charArray);
        if (verbose)
        {
//          printf("> ");
          printf("udpSetSerial called\n");
        }
      }
      break;
    case usbSetName_4Ljava_3lang_3String_2_5V:
      {
        byte *p = word2ptr(paramBase[0]);
        int len;
        Object *charArray = (Object *) word2ptr(get_word_4_ns(fields_start(p)));

        len = get_array_length(charArray);
        if (verbose)
        {
//          printf("> ");
          printf("udpSetName called\n");
        }
      }
      break;
    /*TODO:flashWritePage_4_1BI_5I
    *flashReadPage_4_1BI_5I
    case writePage_4_1BI_5V:
      {
        Object *p = word2ptr(paramBase[0]);
        unsigned long *intArray = (unsigned long *) (((byte *) p) + HEADER_SIZE);
        if (verbose)
        {
          printf("> ");
          printf("writePage called with parameters %x, %d\n", intArray, paramBase[1]);
        }
      }
      return;
    case readPage_4_1BI_5V:
      {
        int i;
        Object *p = word2ptr(paramBase[0]);
        unsigned long *intArray = (unsigned long *) (((byte *) p) + HEADER_SIZE);
        if (verbose)
        {
          printf("> ");
          printf("readPage called with parameters %x, %d\n", intArray, paramBase[1]);
        }
      }
      return;*/
    /*TODO:
    flashExec_4II_5I
    executeProgram_4I_5V
    case exec_4II_5V:
      if (verbose)
      {
        printf("> ");
        printf("exec called\n");
      }
      return;*/
    case playSample_4IIIII_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Playing sound sample\n");
      break;
    case getTime_4_5I:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Sound getTime called\n");
      push_word(0);
      break;
    case getDataAddress_4Ljava_3lang_3Object_2_5I:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      //printf("Data address is %s\n",ptr2word (((byte *) word2ptr (paramBase[0])) + HEADER_SIZE));
      break;
    case gc_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Collecting garbage\n");
      return garbage_collect();//TODO:nxt version
      break;
    /*TODO:
    case diagn_4II_5I:
      push_word (sys_diagn(paramBase[0], paramBase[1]));
      return;*/
    case nanoTime_4_5J:
      break;//TODO:nxt version(need completed)
    case shutDown_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Shutting down\n");
      exit(0);
    case arraycopy_4Ljava_3lang_3Object_2ILjava_3lang_3Object_2II_5V:
      {
        Object *p1 = word2ptr(paramBase[0]);
        Object *p2 = word2ptr(paramBase[2]);
        return arraycopy(p1, paramBase[1], p2, paramBase[3], paramBase[4]);
      }
    case executeProgram_4I_5V:
      {
        /*MethodRecord *mRec;
        ClassRecord *classRecord;
        classRecord = get_class_record (get_entry_class (paramBase[0]));
        // Initialize top word with fake parameter for main():
        set_top_ref_cur (JNULL);
        // Push stack frame for main method:
        mRec= find_method (classRecord, main_4_1Ljava_3lang_3String_2_5V);
        dispatch_special (mRec, curPc);
        dispatch_static_initializer (classRecord, curPc);*/
        return execute_program(paramBase[0]);//TODO:nxt version but not sure
      }
      break;
    case setDebug_4_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");
      printf("Set debug\n");
      break;
    /*TODO:can be cancelled
    case peekWord_4I_5I:
      push_word(*((unsigned long *)(paramBase[0])));
      return;*/
    case eventOptions_4II_5I:
      {
//        if (verbose)
//          printf("> ");
//        else
//          printf("& ");
        printf("Debug event options\n");
        push_word(0);
      }
      break;
    case suspendThread_4Ljava_3lang_3Object_2_5V:
      suspend_thread(ref2ptr(paramBase[0]));
      break;
    case resumeThread_4Ljava_3lang_3Object_2_5V:
      resume_thread(ref2ptr(paramBase[0]));
      break;
    case getUserPages_4_5I:
      push_word(paramBase[0]);//TODO:
      break;
    case getProgramExecutionsCount_4_5I:
      push_word(gProgramExecutions);//TODO:need!!!
      break;
    case setVMOptions_4I_5V://TODO:nxt version
      gVMOptions = paramBase[0];
      break;
    case getVMOptions_4_5I://TODO:nxt version
      push_word(gVMOptions);
      break;
    case memPeek_4III_5I://TODO:nxt version
      push_word(mem_peek(paramBase[0], paramBase[1], paramBase[2]));
      break;
    case memCopy_4Ljava_3lang_3Object_2IIII_5V://TODO:nxt version
      mem_copy(word2ptr(paramBase[0]), paramBase[1], paramBase[2], paramBase[3], paramBase[4]);
      break;
    case getObjectAddress_4Ljava_3lang_3Object_2_5I://TODO:nxt version
      push_word(paramBase[0]);
      break;
    case memGetReference_4II_5Ljava_3lang_3Object_2://TODO:nxt version
      push_word(mem_get_reference(paramBase[0], paramBase[1]));
      break;
    case isAssignable_4II_5Z://TODO:nxt version
      push_word(is_assignable(paramBase[0], paramBase[1]));
      break;
    case registerEvent_4_5I://TODO:nxt version
      push_word(register_event((NXTEvent *) ref2obj(paramBase[0])));
      break;
    case unregisterEvent_4_5I://TODO:nxt version
      push_word(unregister_event((NXTEvent *) ref2obj(paramBase[0])));
      break;
    case changeEvent_4II_5I://TODO:nxt version
      push_word(change_event((NXTEvent *) ref2obj(paramBase[0]), paramBase[1], paramBase[2]));
      break;
    case getFirmwareRevision_4_5I:
      push_word(0);
      break;
    case getFirmwareRawVersion_4_5I:
      push_word((STACKWORD) VERSION_NUMBER);
      break;
    case flashWritePage_4_1BI_5I://TODO:
    {
      //Object *p = word2ptr(p0);
      //unsigned long *intArray = (unsigned long *) jint_array(p);
      //push_word(flash_write_page(intArray,paramBase[1]));
      printf("The function of flashWritePage is unrealized");
      push_word(paramBase[0]);
    }
    break;
     case flashReadPage_4_1BI_5I://TODO:need rewrite!
    {
      //Object *p = word2ptr(p0);
      //unsigned long *intArray = (unsigned long *) jint_array(p);
      //push_word(flash_read_page(intArray,paramBase[1]));
      printf("The function of flashReadPage is unrealized");
      push_word(paramBase[0]);
    }
     break;
    case setHeadAngle_4I_5V:
//      if (verbose)
//         printf("> ");
//      else
//         printf("& ");

      //@deprecated
      //uc_servo_ctrl(paramBase[0]);
      break;
    case readSensorPortValue_4II_5I:
        //TODO deprecated
      //push_word(uc_sensor_get_port_value(paramBase[0],paramBase[1]));
      break;
    case readSensorAllValues_4III_5_1I:
      {
        Object *trace = uc_sensor_get_all_values(paramBase[0], paramBase[1], paramBase[2]);
        if (trace == NULL) return EXEC_RETRY;
        push_word(obj2ref(trace));
      }
      break;
    case control2Motors_4IIIIII_5V:
//        if (verbose)
//            printf("> ");
//        else
//            printf("& ");
        uc_motors_set_speed(paramBase[0],paramBase[1],paramBase[2],paramBase[3],paramBase[4],paramBase[5]);
        break;
    case controlSteeringMotor_4IIII_5V :
//        if (verbose)
//            printf("> ");
//        else
//            printf("& ");
//        printf("%d   %d   %d   %d\n", paramBase[0],paramBase[1],paramBase[2],paramBase[3]);
        uc_servo_motor_ctrl(paramBase[0],paramBase[1],paramBase[2],paramBase[3]);
        break;
    case fileGeneralOperation_4Ljava_3lang_3String_2I_5Z :
        {
            char* fname = string2chp((String*)word2ptr(paramBase[0]));
            int mode = paramBase[1];
            push_word(file_general_operation(fname, mode));
        }
        break;
    case fileQueryOperation_4Ljava_3lang_3String_2I_5I :
        {
            char* fname = string2chp((String*)word2ptr(paramBase[0]));
            int mode = paramBase[1];
            push_word(file_query_operation(fname, mode));
        }
        break;
    case fileWriteOperation_4Ljava_3lang_3String_2Ljava_3lang_3String_2I_5Z :
        {
            char* fname = string2chp((String*)word2ptr(paramBase[0]));
            char* str = string2chp((String*)word2ptr(paramBase[1]));
            int mode = paramBase[2];
            push_word(file_write_operation(fname, str, mode));
        }
        break;
    case fileReadOperation_4Ljava_3lang_3String_2_1BIII_5I :
        {
            char* fname = string2chp((String*)word2ptr(paramBase[0]));
            Object *p = word2ptr(paramBase[1]);
            byte *byteArray = (((byte *) p) + HEADER_SIZE);
            char *buf = (char *)byteArray;
            int length = paramBase[2];
            int mode = paramBase[3];
            int param = paramBase[4];
            push_word(file_read_operation(fname, buf, length, mode, param));
        }
        break;
    case fileSpecificQueryOperation_4Ljava_3lang_3String_2_1BII_5I :
        {
            char* fname = string2chp((String*)word2ptr(paramBase[0]));
            Object *p1 = word2ptr(paramBase[1]);
            byte *byteArray = (((byte *) p1) + HEADER_SIZE);
            char *buf = (char *)byteArray;
            int len = paramBase[2];
            int num = paramBase[3];
            push_word(file_specific_query_operation(fname, buf, len, num));
        }
        break;
    case wifiConnect_4Ljava_3lang_3String_2I_5I :
        {
            char* servAddr = string2chp((String*)word2ptr(paramBase[0]));
            int servPort = paramBase[1];
            push_word(wifi_connect_server(servAddr, servPort));
        }
        break;
    case wifiWrite_4Ljava_3lang_3String_2I_5I :
        {
            char* buf = string2chp((String*)word2ptr(paramBase[0]));
            int len = paramBase[1];
            push_word(wifi_send_to_server(buf, len));
        }
        break;
    case wifiRead_4_1BI_5I :
        {
            Object *p1 = word2ptr(paramBase[0]);
            byte *byteArray = (((byte *) p1) + HEADER_SIZE);
            char *buf = (char *)byteArray;
            int len = paramBase[1];
            push_word(wifi_receive_from_server(buf, len));
        }
        break;
    case wifiDisconnect_4_5V :
        {
            wifi_disconnect_server();
        }
        break;
    /*case getFirmwareMajorVersion_4_5I:
      push_word((STACKWORD) MAJOR_VERSION);
      return;
    case getFirmwareMinorVersion_4_5I:
      push_word((STACKWORD) MINOR_VERSION);
      return;*/
    case servoAngleQuery_4I_5I:
    {
        int id = paramBase[0];
        push_word(uc_servo_get_angle(id));
        break;
    }
    case singleServoAngleCtrl_4II_5V:
    {
        int id = paramBase[0];
        int angle = paramBase[1];
        uc_servo_angle_ctrl(id, angle);
        break;
    }
    case broadcastServoAngleCtrl_4IIII_5V:
    {
        int angle1 = paramBase[0];
        int angle2 = paramBase[1];
        int angle3 = paramBase[2];
        int angle4 = paramBase[3];
        uc_servo_angle_group_ctrl(angle1, angle2, angle3, angle4);
        break;
    }
    case motorTachoCountQuery_4I_5I:
    {
        int id = paramBase[0];
        push_word(uc_motor_get_count(id));
        break;
    }
    case motorTachoCountReset_4I_5V:
    {
        int id = paramBase[0];
        uc_motor_reset_count(id);
        break;
    }
    case singleMotorPowerCtrl_4IIII_5V:
    {
        int id = paramBase[0];
        int power = paramBase[1];
        int dis = paramBase[2];
        int angle = paramBase[3];
        uc_motor_power_single_ctrl(id, power, dis, angle);
        break;
    }
    case broadcastMotorPowerCtrl_4_1I_1I_1I_1I_5V:
    {
        Object *p1 = word2ptr(paramBase[0]);
        byte *ba1 = (((byte *) p1) + HEADER_SIZE);
        int *param1 = (int *)ba1;

        Object *p2 = word2ptr(paramBase[1]);
        byte *ba2 = (((byte *) p2) + HEADER_SIZE);
        int *param2 = (int *)ba2;

        Object *p3 = word2ptr(paramBase[2]);
        byte *ba3 = (((byte *) p3) + HEADER_SIZE);
        int *param3 = (int *)ba3;

        Object *p4 = word2ptr(paramBase[3]);
        byte *ba4 = (((byte *) p4) + HEADER_SIZE);
        int *param4 = (int *)ba4;

        uc_motor_power_group_ctrl(param1, param2, param3, param4);
        break;
    }
    case singleMotorSpeedCtrl_4IIII_5V:
    {
        int id = paramBase[0];
        int speed = paramBase[1];
        int dis = paramBase[2];
        int angle = paramBase[3];

        uc_motor_speed_single_ctrl(id, speed, dis, angle);
        break;
    }
    case broadcastMotorSpeedCtrl_4_1I_1I_1I_1I_5V:
    {
        Object *p1 = word2ptr(paramBase[0]);
        byte *ba1 = (((byte *) p1) + HEADER_SIZE);
        int *param1 = (int *)ba1;

        Object *p2 = word2ptr(paramBase[1]);
        byte *ba2 = (((byte *) p2) + HEADER_SIZE);
        int *param2 = (int *)ba2;

        Object *p3 = word2ptr(paramBase[2]);
        byte *ba3 = (((byte *) p3) + HEADER_SIZE);
        int *param3 = (int *)ba3;

        Object *p4 = word2ptr(paramBase[3]);
        byte *ba4 = (((byte *) p4) + HEADER_SIZE);
        int *param4 = (int *)ba4;

        uc_motor_speed_group_ctrl(param1, param2, param3, param4);
        break;
    }
    default:
#ifdef DEBUG_METHODS
      printf("Received bad native method code: %d\n", signature);
#endif
      //throw_exception (JAVA_LANG_NOSUCHMETHODERROR);
    }
    return EXEC_CONTINUE;
}

