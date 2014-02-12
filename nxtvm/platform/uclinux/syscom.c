#include     <stdio.h>      /*标准输入输出定义*/
#include     <stdlib.h>     /*标准函数库定义*/
#include     <unistd.h>     /*Unix标准函数定义*/
#include     <sys/types.h>  /* */
#include     <sys/stat.h>   /* */
#include     <fcntl.h>      /*文件控制定义*/
#include     <termios.h>    /*PPSIX终端控制定义*/
#include     <errno.h>      /*错误号定义*/
#include     "syscom.h"
#include     "constants.h"

/*
Very important!!!
if it is running in old car, please use #define OLD_CAR,
else it is running in new car, pleas use #define NEW_CAR,
WARNING: DON'T USE "#define OLD_CAR" AND "#define NEW_CAR" AT THE SAME TIME
*/
//#define OLD_CAR
#define OLD_CAR

//#ifdef OLD_CAR
int ufd;//串口文件号
int maxfd;
fd_set rfds;
//#endif

//#ifdef NEW_CAR
int ufd_sensor;//传感器串口文件号
int maxfd_sensor;
fd_set rfds_sensor;

int ufd_motor;//马达串口文件号
int maxfd_motor;
fd_set rfds_motor;
//#endif

int reversel;
int reverser;

unsigned char frame[50];//数据帧
unsigned char receive[100];//接收到的数据暂时存放在这里

void printFrame(int l){
  		int length = l+6;
        int i;
  		for(i=0;i<length;i++)
  		{
  			//cout<<hex<<(int)frame[i]<<" ";
            printf("%X ",frame[i]);
  		}
  		//cout<<endl;
        printf("\n");
}

void printFrameRev(){
  		int length = receive[4]+6;
        int i;
  		for(i=0;i<length;i++)
  		{
  			//cout<<hex<<(int)frame[i]<<" ";
            printf("%X ",receive[i]);
  		}
  		//cout<<endl;
        printf("\n");
}

void setDev()
{
#ifdef OLD_CAR
    struct termios  opt;
	//int fd=getUfd();
	tcgetattr(ufd,&opt);
	tcflush(ufd,TCIOFLUSH);
	cfsetispeed(&opt,B19200);
	cfsetospeed(&opt,B19200);
	opt.c_cflag |=(CLOCAL | CREAD);//本地连接和接受使能
	opt.c_cflag &=~CSIZE;
	opt.c_cflag |= CS8;//设置字符大小
	opt.c_cflag &= ~PARENB;//无奇偶校验
	opt.c_cflag &= ~CSTOPB;//1位停止位
	opt.c_cflag &= ~CRTSCTS;
	opt.c_lflag &=~(ICANON | ECHO | ECHOE | ISIG);
	opt.c_iflag &= ~(IXON | IXOFF | IXANY);	// 关闭流控制//为解决bug1而添加
	opt.c_oflag &= ~OPOST;//为解决bug1而添加

	opt.c_cc[VMIN]=1;
	opt.c_cc[VTIME]=0;

	opt.c_iflag &= INPCK;//

	tcsetattr(ufd,TCSANOW,&opt);//保存设置
	tcflush(ufd,TCIOFLUSH);
	if(tcsetattr(ufd,TCSANOW,&opt)!=0)
	{
		perror("Set Speed Error!\n");
		return ;
	}
#endif

#ifdef NEW_CAR
    //设置传感器串口
    struct termios opt_sensor;
	tcgetattr(ufd_sensor,&opt_sensor);
	tcflush(ufd_sensor,TCIOFLUSH);
    cfsetispeed(&opt_sensor,B57600);
	cfsetospeed(&opt_sensor,B57600);
	opt_sensor.c_cflag |=(CLOCAL | CREAD);//本地连接和接受使能
	opt_sensor.c_cflag &=~CSIZE;
	opt_sensor.c_cflag |= CS8;//设置字符大小
	opt_sensor.c_cflag &= ~PARENB;//无奇偶校验
	opt_sensor.c_cflag &= ~CSTOPB;//1位停止位
	opt_sensor.c_cflag &= ~CRTSCTS;
	opt_sensor.c_lflag &=~(ICANON | ECHO | ECHOE | ISIG);
	opt_sensor.c_iflag &= ~(IXON | IXOFF | IXANY);	// 关闭流控制//为解决bug1而添加
	opt_sensor.c_oflag &= ~OPOST;//为解决bug1而添加
	opt_sensor.c_cc[VMIN]=1;
	opt_sensor.c_cc[VTIME]=0;
	opt_sensor.c_iflag &= INPCK;//
	tcsetattr(ufd_sensor,TCSANOW,&opt_sensor);//保存设置
	tcflush(ufd_sensor,TCIOFLUSH);
	if(tcsetattr(ufd_sensor,TCSANOW,&opt_sensor)!=0)
	{
		perror("Set Speed Error!\n");
		return ;
	}

	//设置马达串口
    struct termios opt_motor;
	tcgetattr(ufd_motor,&opt_motor);
	tcflush(ufd_motor,TCIOFLUSH);
    cfsetispeed(&opt_motor,B115200);
	cfsetospeed(&opt_motor,B115200);
	opt_motor.c_cflag |=(CLOCAL | CREAD);//本地连接和接受使能
	opt_motor.c_cflag &=~CSIZE;
	opt_motor.c_cflag |= CS8;//设置字符大小
	opt_motor.c_cflag &= ~PARENB;//无奇偶校验
	opt_motor.c_cflag &= ~CSTOPB;//1位停止位
	opt_motor.c_cflag &= ~CRTSCTS;
	opt_motor.c_lflag &=~(ICANON | ECHO | ECHOE | ISIG);
	opt_motor.c_iflag &= ~(IXON | IXOFF | IXANY);	// 关闭流控制//为解决bug1而添加
	opt_motor.c_oflag &= ~OPOST;//为解决bug1而添加
	opt_motor.c_cc[VMIN]=1;
	opt_motor.c_cc[VTIME]=0;
	opt_motor.c_iflag &= INPCK;//
	tcsetattr(ufd_motor,TCSANOW,&opt_motor);//保存设置
	tcflush(ufd_motor,TCIOFLUSH);
	if(tcsetattr(ufd_motor,TCSANOW,&opt_motor)!=0)
	{
		perror("Set Speed Error!\n");
		return ;
	}
#endif
}

void openDev()
{
#ifdef OLD_CAR
    ufd = open("/dev/tts/2", O_RDWR|O_NOCTTY|O_NONBLOCK);
    if (-1==ufd)//如果打开失败就退出，否则对它进行初始化
    {
        perror("Can't Open Serial Port");
        exit(-1);
    }
    else
	{
		fcntl(ufd, F_SETFL, FNDELAY);
		setDev();
        maxfd = ufd+1;
		FD_ZERO(&rfds);// 清空串口接收端口集
        FD_SET(ufd,&rfds);// 设置串口接收端口集
	}
#endif

#ifdef NEW_CAR
    ufd_sensor = open("/dev/ttySAC1", O_RDWR|O_NOCTTY|O_NONBLOCK);
    ufd_motor = open("/dev/ttySAC2", O_RDWR|O_NOCTTY|O_NONBLOCK);
    if (-1==ufd_sensor || -1==ufd_motor)//如果打开失败就退出，否则对它进行初始化
    {
        perror("Can't Open Serial Port");
        exit(-1);
    }

    fcntl(ufd_sensor, F_SETFL, FNDELAY);
    fcntl(ufd_motor, F_SETFL, FNDELAY);
    setDev();
    maxfd_sensor = ufd_sensor+1;
    maxfd_motor = ufd_motor+1;
    FD_ZERO(&rfds_sensor);// 清空串口接收端口集
    FD_ZERO(&rfds_motor);// 清空串口接收端口集
    FD_SET(ufd_sensor,&rfds_sensor);// 设置串口接收端口集
    FD_SET(ufd_motor,&rfds_motor);// 设置串口接收端口集
#endif
}

unsigned int frameOK(unsigned char * readBuffer)
{
  	 unsigned char i,length,k;
	   if(*readBuffer==0x55)//先检测数据头
	   {
	   		 ++readBuffer;
		}
	   else
	   {
	  		 return 1;
	   }
		if(*readBuffer==0xAA)
	   {
	   		++readBuffer;
	   }
		else
	   {
	  		 return 1;
	   }
		++readBuffer;//跳过地址检查
		++readBuffer;
		length=*readBuffer;
		k=0;
		for(i=0;i<length;i++)//下面检测校验和
		{
			++readBuffer;
	  	    k+=*readBuffer;
		}
	  	  ++readBuffer;
	 	   k=~k;
		if(k==*readBuffer)
		{
	   		return 0;
		}
		else
		{
	   		perror("FrameWrong!\n");
	   	 	return 1;
		}
}

/*
*这个函数是对<unistd.h>中write（）函数的包装，用来向串口写数据
*/
unsigned int COMM_WriteUart(int uartFD,
						 unsigned char * writeBuffer,
						 unsigned int writeNum)
{
    int r=-1;
    while(r==-1)
    {
        tcflush(uartFD,TCIOFLUSH);//先刷新缓冲区
        r=write(uartFD,writeBuffer,writeNum);//如果写失败，r＝－1，则继续重写。
        /*
		 * At this place, sleeping 6000us is a special treatment to prevent the chip not handling
		 * next frame. After some experimental tests, 6000us should be the mininum value.
		 * To do: to find the reason!
		 */
        //usleep(6000);//等待数据写完。!!!!!!!!!!!!!!!6000
        //printf("r= %d\n",r);
    }
	return r;
}

/*
*这个函数是对<unistd.h>中write（）函数的包装，用来读取串口的数据。
*如果读到了全部需要的数据返回0，否则返回1.
*/
unsigned int COMM_ReadUart(int uartFD,
						unsigned char * readBuffer,
						unsigned int readNum)
{
    unsigned char * buffer= readBuffer;
	bzero(readBuffer,readNum);//将热readBuffer中数据清零。
	int num;//读到的数据数

#ifdef OLD_CAR
	FD_ZERO(&rfds);// 清空串口接收端口集
    FD_SET(uartFD, &rfds);// 设置串口接收端口集
#endif

#ifdef NEW_CAR
    if(uartFD == ufd_sensor)
    {
        FD_ZERO(&rfds_sensor);// 清空串口接收端口集
        FD_SET(uartFD, &rfds_sensor);// 设置串口接收端口集
    }else{
        FD_ZERO(&rfds_motor);// 清空串口接收端口集
        FD_SET(uartFD, &rfds_motor);// 设置串口接收端口集
    }
#endif

	while(true)
	{
#ifdef OLD_CAR
        select(maxfd, &rfds, NULL, NULL, NULL);
        if (FD_ISSET(uartFD,&rfds))
            break;
#endif

#ifdef NEW_CAR
        if(uartFD == ufd_sensor)
        {
            select(maxfd_sensor, &rfds_sensor, NULL, NULL, NULL);
            if (FD_ISSET(uartFD,&rfds_sensor))
                break;
        }else{
            select(maxfd_motor, &rfds_motor, NULL, NULL, NULL);
            if (FD_ISSET(uartFD,&rfds_motor))
                break;
        }
#endif

        usleep(100);
	}

	do//读3次如果还没有读完就失败返回。
	{
		num = read(uartFD,buffer,readNum);

		if(num!=-1)
		{
			buffer+=num;
			readNum-=num;
		}
		else if(num==-1)
		{
            if(errno == EAGAIN)
            {
                num = 0;
            }else
            {
                perror("The Uart reading failured!\n");//here!
			    exit(1);
            }
		}
		usleep(500);
	}while (readNum); // && (errno == 0 || errno == EINTR)
	tcflush(uartFD,TCIOFLUSH);//刷新缓冲区
	unsigned int t=frameOK(readBuffer);//进行数据检测
	if(readNum||t==1)
	{
		return 1;
	}
	return 0;
}

void initFrameHead(unsigned char length,unsigned char cmd)
{
  	frame[0]=0x55;
	frame[1]=0xAA;
	frame[3]=0x00;
	//frame length
	frame[4]=length;
	//cmd type
	frame[5]=cmd;
}

void setDstAddr(char dst)
{
  	frame[2]=dst;
}

void calcCRC(unsigned int datalen){
  	int i;
		int j=5+datalen;
		frame[j]=0;
		for(i=5;i<j;i++)
		{
			frame[j]=frame[j]+frame[i];
		}
		frame[j]=~frame[j];
}

int cToInt(char c)
{
  	int num;
	unsigned char byte[2];
	int i;
	byte[0]=c&0xF0;
	byte[0]=byte[0]>>4;
	byte[1]=c&0x0F;
	num=byte[0]*16+byte[1];
	return num;
}

/**
  * 设置电机功率控制帧信息
  */
void setFrame4MotorPower(int power, int dis, int angle, int startAddr)
{
    //设置PWM值
    if(power!=0)
    {
        //向前
        if(power>0)
        {
            //惰行
            if(power>100)
            {
                frame[startAddr] = 255;
                frame[startAddr+1] = 0;
            }
            //调功
            else
            {
                frame[startAddr] = power*2.5;
                frame[startAddr+1] = 0;
            }
        }
        //向后
        else
        {
            //惰行
            if(power<-100)
            {
                frame[startAddr] = 255;
                frame[startAddr+1] = 0xFF;
            }
            //调功
            else
            {
                frame[startAddr] = -power*2.5;
                frame[startAddr+1] = 0xFF;
            }
        }
    }
    //刹车
    else
    {
        frame[startAddr] = 0;
        frame[startAddr+1] = 0;
    }

    //设置运行脉冲数
    int pulse = (int)(dis*100/251);
    frame[startAddr+2]=(unsigned char)pulse%256;
    frame[startAddr+3]=(unsigned char)(pulse/256);

    //设置舵机角度值
    frame[startAddr+4]=(unsigned char)(angle)%256;
  	frame[startAddr+5]=(unsigned char)((angle)/256);
}



/**
  * 设置电机速度控制帧信息
  */
void setFrame4MotorSpeed(int speed, int dis, int angle, int startAddr)
{
    //设置速度值
    frame[startAddr]=(char)speed%256;
    frame[startAddr+1]=(char)(speed/256);

    //设置运行脉冲数
    int pulse = (int)(dis*100/251);
    frame[startAddr+2]=(unsigned char)pulse%256;
    frame[startAddr+3]=(unsigned char)(pulse/256);

    //设置舵机角度值
    frame[startAddr+4]=(unsigned char)(angle)%256;
  	frame[startAddr+5]=(unsigned char)((angle)/256);
}

/** 根据端口号获得传感器数据
 *
 *  @param id 传感器编号
 *
 *  @param mode 工作模式<br>
 *              0:测试数据不作处理,直接返回<br>
 *              1:剔除测量数据中的最大最小值后剩余数据返回<br>
 *              2:将测量数据取平均后返回平均值<br>
 *              4:剔除测量数据中的最大最小值后平均值返回
 *
 *  @param measureCount 测量次数,范围1-7;当值小于4时,置工作模式为0
 *
 */
Object * uc_sensor_get_all_values(int id, int mode, int measureCount)
{
    int workMode = 0x20;
    switch(mode)
    {
        case 0:
            workMode = 0x20;
            break;
        case 1:
            workMode = 0x21;
            break;
        case 2:
            workMode = 0x22;
            break;
        case 3:
            workMode = 0x23;
            break;
        default:
            perror("Error mode!\n");
            return JNULL;
    }

    int workParam = 1;
    if(measureCount>=4 &&measureCount<=7)
    {
        workParam = measureCount;
    }
    else if(measureCount>=1&&measureCount<4){
        workMode = 0x20;
        workParam = measureCount;
    }
    else
    {
        perror("Error measureCount!\n");
        return JNULL;
    }

    JINT *datas;
    Object *stackArray;
    int dataCnt = 2;

    initFrameHead(0x03,0x03);
    frame[6] = workMode;//set work mode
    frame[7] = workParam;//set work parameter
    if(id<4)
    {

#ifdef OLD_CAR
        setDstAddr((char)(id+2));

        calcCRC(3);

        //printFrame(3);
        COMM_WriteUart(ufd,frame,9);

        int readLength = 7;
        switch(workMode)
        {
            case 0x20:
                readLength += 4*measureCount;
                dataCnt = 2*measureCount;
                break;
            case 0x21:
                readLength += 4*(measureCount-2);
                dataCnt = 2*measureCount;
                break;
            case 0x22:
                readLength += 4;
                dataCnt = 2;
                break;
            case 0x23:
                readLength += 4;
                dataCnt = 2;
                break;
        }

        if(COMM_ReadUart(ufd,receive,readLength)==1)
        {
            return JNULL;
        }
#endif

#ifdef NEW_CAR
        if(id>2)
        {
            perror("No such sensor port!\n");
            return JNULL;
        }
        setDstAddr((char)(id));
        calcCRC(3);
        //printFrame(3);
        COMM_WriteUart(ufd_sensor,frame,9);
        int readLength = 7;
        switch(workMode)
        {
            case 0x20:
                readLength += 8*measureCount;
                dataCnt = 4*measureCount;
                break;
            case 0x21:
                readLength += 8*(measureCount-2);
                dataCnt = 4*measureCount;
                break;
            case 0x22:
                readLength += 8;
                dataCnt = 4;
                break;
            case 0x23:
                readLength += 8;
                dataCnt = 4;
                break;
        }

       // printFrame(3);
        if(COMM_ReadUart(ufd_sensor,receive,readLength)==1)
        {
            return JNULL;
        }
        //printFrameRev();
#endif

    }
    else
    {
        perror("No such sensor port!\n");
        return JNULL;
    }
    // Try and allocate the space for the trace.
    stackArray = new_single_array(AI, dataCnt);
    if (stackArray == JNULL) return JNULL;
    datas = jint_array(stackArray);

    int i=0;
    for(i=0;i<dataCnt;i++)
    {
        datas[i]=cToInt(receive[6+i*2])+cToInt(receive[7+i*2])*256;
    }
    return stackArray;
}


/**
  * 舵机角度查询
  *
  * @param id 舵机编号
  */
int uc_servo_get_angle(int id)
{
    int angle = -1;
#ifdef NEW_CAR
    initFrameHead(4, 0x01);
    setDstAddr(id);
    frame[6] = 0x2E;
    frame[7] = 0x00;
    frame[8] = 2;
    calcCRC(4);

    COMM_WriteUart(ufd_motor, frame, 10);

    if(COMM_ReadUart(ufd_motor,receive,12)==0)
	{
		angle = cToInt(receive[9])*256+cToInt(receive[10]);
	}
#endif

    return angle;
}

/**
  * 舵机角度控制
  * @param id 舵机编号
  * @param angle 角度
  */
void uc_servo_angle_ctrl(int id, int angle)
{
#ifdef NEW_CAR
    initFrameHead(3, 0x14);
    setDstAddr(id);
    frame[6]=(unsigned char)(angle)%256;
  	frame[7]=(unsigned char)((angle)/256);

  	calcCRC(3);
  	COMM_WriteUart(ufd_motor,frame,9);
  	COMM_ReadUart(ufd_motor,receive,9);
#endif
}

/**
  * 舵机角度成组控制
  *
  * @param a1 1号舵机
  * @param a2 2号舵机
  * @param a3 3号舵机
  * @param a4 4号舵机
  */
void uc_servo_angle_group_ctrl(int a1, int a2, int a3, int a4)
{
#ifdef NEW_CAR
    initFrameHead(9,0x14);
    setDstAddr(0x99);
    frame[6]=(unsigned char)(a1)%256;
  	frame[7]=(unsigned char)((a1)/256);

  	frame[8]=(unsigned char)(a2)%256;
  	frame[9]=(unsigned char)((a2)/256);

  	frame[10]=(unsigned char)(a3)%256;
  	frame[11]=(unsigned char)((a3)/256);

  	frame[12]=(unsigned char)(a4)%256;
  	frame[13]=(unsigned char)((a4)/256);

    calcCRC(9);

    COMM_WriteUart(ufd_motor,frame,15);
#endif
}

/** 获得马达转圈数 */
int uc_motor_get_count(int id)
{
#ifdef OLD_CAR
	int angle;
	initFrameHead(0x02,0x05);
	setDstAddr((char)1);
	frame[6] = (char)id;
	calcCRC(0x02);

	COMM_WriteUart(ufd,frame,8);

	if(COMM_ReadUart(ufd,receive,10)==0)
	{
		angle = cToInt(receive[7])+cToInt(receive[8])*256;
	}

    //printf("------angle=%d------\n", angle);

    if(id == 0){
        if(reversel == 0)
        {
            return angle;
        }
        else if(reversel == 1)
        {
            return (-angle);
        }
    }
    if(id == 1){
        if(reverser == 0)
        {
            return angle;
        }
        else if(reverser == 1)
        {
            return (-angle);
        }
    }
#endif

#ifdef NEW_CAR
    int pulseCount;

    initFrameHead(4, 0x01);
    setDstAddr(id);
    frame[6] = 0x38;
    frame[7] = 0x00;
    frame[8] = 2;
    calcCRC(4);

    COMM_WriteUart(ufd_motor, frame, 10);

    if(COMM_ReadUart(ufd_motor,receive,12)==0)
	{
		pulseCount = cToInt(receive[9])*256+cToInt(receive[10]);
	}

	return 3.6*pulseCount;
#endif
}

/** 重置马达转圈数 */
void uc_motor_reset_count(int id)
{
#ifdef OLD_CAR
	initFrameHead(0x02,0x04);
	setDstAddr((char)1);
	frame[6] = (char)id;
	calcCRC(0x02);

	COMM_WriteUart(ufd,frame,8);
    if(COMM_ReadUart(ufd,receive,8)!=0){
        perror("The frame received is wrong!\n");
    }
#endif

#ifdef NEW_CAR
    initFrameHead(6, 0x02);
    setDstAddr(id);
    frame[6] = 0x38;
    frame[7] = 0x00;
    frame[8] = 2;
    frame[9] = 0x00;
    frame[10] = 0x00;
    calcCRC(6);

    COMM_WriteUart(ufd_motor, frame, 12);

    if(COMM_ReadUart(ufd_motor,receive,10)==0)
	{

	}
#endif
}

/**
  * 单电机功率、舵机角度控制
  *
  * @param id
  *            电机编号
  * @param power
  *            功率(前进：1-100调功 >100惰性) (后退：-1 - -100调功 <-100惰性) (0刹车)
  * @param dis
  *            行走距离>=0, 0一直行走
  * @param angle
  *            舵机角度0-1800
  */
void uc_motor_power_single_ctrl(int id, int power, int dis, int angle)
{
#ifdef NEW_CAR
    initFrameHead(7, 0x11);
    setDstAddr(id);

    //设置PWM值
    if(power!=0)
    {
        //向前
        if(power>0)
        {
            //惰行
            if(power>100)
            {
                frame[6] = 255;
                frame[7] = 0;
            }
            //调功
            else
            {
                frame[6] = power*2.5;
                frame[7] = 0;
            }
        }
        //向后
        else
        {
            //惰行
            if(power<-100)
            {
                frame[6] = 255;
                frame[7] = 0xFF;
            }
            //调功
            else
            {
                frame[6] = -power*2.5;
                frame[7] = 0xFF;
            }
        }
    }
    //刹车
    else
    {
        frame[6] = 0;
        frame[7] = 0;
    }

    //设置运行脉冲数
    int pulse = (int)(dis*100/251);

    frame[8]=(unsigned char)pulse%256;
    frame[9]=(unsigned char)(pulse/256);

    //设置舵机角度值
    frame[10]=(unsigned char)(angle)%256;
  	frame[11]=(unsigned char)((angle)/256);

    calcCRC(7);

    //printFrame(7)
    COMM_WriteUart(ufd_motor, frame, 13);

    COMM_ReadUart(ufd_motor, receive, 9);
#endif
}

/**
  * 电机功率、舵机角度成组控制
  * 参数定义：
  *     array[0]:功率 (前进：1-100调功 >100惰性) (后退：-1 - -100调功 <-100惰性) (0刹车)
  *     array[1]:行走距离>=0, 0一直行走
  *     array[2]:舵机角度0-1800
  *
  * @param param1 1号舵机设置参数
  * @param param2 2号舵机设置参数
  * @param param3 3号舵机设置参数
  * @param param4 4号舵机设置参数
  */
void uc_motor_power_group_ctrl(int* param1, int* param2, int* param3, int* param4)
{
#ifdef NEW_CAR
    initFrameHead(25, 0x11);
    setDstAddr(0x99);

    int power, dis, angle;
    power = param1[0];
    dis = param1[1];
    angle = param1[2];
    setFrame4MotorPower(power, dis, angle, 6);

    power = param2[0];
    dis = param2[1];
    angle = param2[2];
    setFrame4MotorPower(power, dis, angle, 12);

    power = param3[0];
    dis = param3[1];
    angle = param3[2];
    setFrame4MotorPower(power, dis, angle, 18);

    power = param4[0];
    dis = param4[1];
    angle = param4[2];
    setFrame4MotorPower(power, dis, angle, 24);

    calcCRC(25);

    COMM_WriteUart(ufd_motor, frame, 31);

#endif
}

/**
  * 单电机功率、舵机角度控制
  *
  * @param id
  *            电机编号
  * @param speed
  *            速度: >0前进, <0后退, ==0刹车
  * @param dis
  *            行走距离>=0, 0一直行走
  * @param angle
  *            舵机角度0-1800
  */
void uc_motor_speed_single_ctrl(int id, int speed, int dis, int angle)
{
#ifdef NEW_CAR
    initFrameHead(7, 0x13);
    setDstAddr(id);

    //设置速度值
    frame[6]=(char)speed%256;
    frame[7]=(char)(speed/256);

    //设置运行脉冲数
    int pulse = (int)(dis*100/251);
    frame[8]=(unsigned char)pulse%256;
    frame[9]=(unsigned char)(pulse/256);

    //设置舵机角度值
    frame[10]=(unsigned char)(angle)%256;
  	frame[11]=(unsigned char)((angle)/256);

    calcCRC(7);

    COMM_WriteUart(ufd_motor, frame, 13);

    COMM_ReadUart(ufd_motor, receive, 9);
#endif
}

/**
  * 电机速度、舵机角度成组控制
  * 参数定义：array[0]:速度; array[1]:行走距离; array[2]:舵机角度
  *
  * @param param1 1号舵机设置参数
  * @param param2 2号舵机设置参数
  * @param param3 3号舵机设置参数
  * @param param4 4号舵机设置参数
  */
void uc_motor_speed_group_ctrl(int* param1, int* param2, int* param3, int* param4)
{
    initFrameHead(25, 0x13);
    setDstAddr(0x99);

    int speed, dis, angle;
    speed = param1[0];
    dis = param1[1];
    angle = param1[2];
    setFrame4MotorSpeed(speed, dis, angle, 6);

    speed = param2[0];
    dis = param2[1];
    angle = param2[2];
    setFrame4MotorSpeed(speed, dis, angle, 12);

    speed = param3[0];
    dis = param3[1];
    angle = param3[2];
    setFrame4MotorSpeed(speed, dis, angle, 18);

    speed = param4[0];
    dis = param4[1];
    angle = param4[2];
    setFrame4MotorSpeed(speed, dis, angle, 24);

    calcCRC(25);

#ifdef NEW_CAR
    COMM_WriteUart(ufd_motor, frame, 31);
#endif
}

void uc_motor_set_speed(int id, int power, int mode)
{
	initFrameHead(0x04,0x03);
	setDstAddr((char)1);

    if(id == 0){
        frame[6] = 0x00;
        if(power>0)
        {
            reversel=0;
        }
        else if(power<0)
        {
            reversel=1;
        }
    }
    if(id == 1){
        frame[6] = 0x01;
         if(power>0)
        {
            reverser=0;
        }
        else if(power<0)
        {
            reverser=1;
        }
    }
	/*
     *This version of code is used specially for YM.
    switch(mode)
	{
		case 1:
			frame[7] = (char)power;
			frame[8] = 0;
			break;
		case 2:
			frame[7] = (char)power;
			frame[8] = 1;
			break;
		case 3:
			frame[7] = 0;
			frame[8] = 0;
			break;
		case 4:
			frame[7] = 0xFF;
			frame[8] = 0;
			break;
        default:
            break;
	}*/
    /*
    *The version blow is for Lego.
    */
    if(power != 0)
    {
        if(power>0)//forward
        {
            frame[7] = (char)(power*2.5);//make the power suit for native motor
            frame[8] = 0;
        }
        else//backward
        {
            frame[7] = (char)(-power*2.5);
            frame[8] = 1;
        }
    }
    else
    {
        if(mode == 1)//stop
        {
            frame[7] = 0;
            frame[8] = 0;
        }
        else//float
        {
            frame[7] = 0xFF;
            frame[8] = 0;
        }
    }
	calcCRC(4);
    //printFrame(0x04);
	COMM_WriteUart(ufd,frame,10);

    if(COMM_ReadUart(ufd,receive,9)!=0){
        perror("The frame received is wrong!\n");
    }
}


void uc_servo_ctrl(int a1, int a2, int a3, int a4)
{
    printf("---------- deprecated method ---------\n");
/*
    initFrameHead(0x02,0x14);
    setDstAddr((char)id);
    frame[6] = (unsigned char)angles[0];
    calcCRC(2);

    COMM_WriteUart(ufd,frame,8);

    if(COMM_ReadUart(ufd,receive,9)!=0)
    {
        perror("The frame received is wrong!\n");
    }
*/
}

void uc_motors_set_speed(int power1, int mode1, int dis1, int power2, int mode2, int dis2)
{
    initFrameHead(0x09,0x07);
	setDstAddr((char)1);
    //set power1
    if(power1 != 0)
    {
        if(power1 > 0)//forward
        {
            frame[6] = (char)(power1*2.5);//make the power suit for native motor
            frame[7] = 0;
        }
        else//backward
        {
            frame[6] = (char)(-power1*2.5);
            frame[7] = 1;
        }
    }
    else
    {
        if(mode1 == 1)//stop
        {
            frame[6] = 0;
            frame[7] = 0;
        }
        else//float
        {
            frame[6] = 0xFF;
            frame[7] = 0;
        }
    }
    //set power2
    if(power2 != 0)
    {
        if(power2 > 0)//forward
        {
            frame[8] = (char)(power2*2.5);//make the power suit for native motor
            frame[9] = 0;
        }
        else//backward
        {
            frame[8] = (char)(-power2*2.5);
            frame[9] = 1;
        }
    }
    else
    {
        if(mode2 == 1)//stop
        {
            frame[8] = 0;
            frame[9] = 0;
        }
        else//float
        {
            frame[8] = 0xFF;
            frame[9] = 0;
        }
    }
    /*frame[10] = (unsigned char)0;
    frame[11] = (unsigned char)0;
    frame[12] = (unsigned char)0;
    frame[13] = (unsigned char)0;*/
    int pulse1 = (int)(dis1*50/43);
    int pulse2 = (int)(dis2*50/43);
    frame[10]=(unsigned char)pulse1%256;
    frame[11]=(unsigned char)(pulse1/256);
    frame[12]=(unsigned char)pulse2%256;
    frame[13]=(unsigned char)(pulse2/256);
    calcCRC(0x09);

    //printf("left distance:%d, right distance:%d\n", pulse1, pulse2);
    //printFrame(15);

    COMM_WriteUart(ufd,frame,15);

    if(COMM_ReadUart(ufd,receive,8)!=0){
        perror("The frame received is wrong!\n");
    }
}

void uc_servo_motor_ctrl(int angle, int power, int mode, int dis){
    initFrameHead(0x07,0x11);
	setDstAddr((char)1);
    if(power != 0)
    {
        if(power > 0)//forward
        {
            frame[6] = (char)(power*2.5);//make the power suit for native motor
            frame[7] = 0;
        }
        else//backward
        {
            frame[6] = (char)(-power*2.5);
            frame[7] = 1;
        }
    }
    else
    {
        if(mode == 1)//stop
        {
            frame[6] = 0;
            frame[7] = 0;
        }
        else//float
        {
            frame[6] = 0xFF;
            frame[7] = 0;
        }
    }
    int pulse = (int)(dis*125/157);
    frame[8]=(unsigned char)pulse%256;
    frame[9]=(unsigned char)(pulse/256);
    frame[10]=(unsigned char)(angle*10)%256;
  	frame[11]=(unsigned char)((angle*10)/256);

    calcCRC(0x07);
//    printFrame(0x07);
    COMM_WriteUart(ufd,frame,13);
    if(COMM_ReadUart(ufd,receive,9)!=0){
        perror("The frame received is wrong!\n");
    }
}

