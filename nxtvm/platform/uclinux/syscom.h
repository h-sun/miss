#ifndef _SYSCOM_H
#define _SYSCOM_H

#include     "classes.h"
#include     "memory.h"

/** 打开串口设备 */
extern void openDev();

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
extern Object * uc_sensor_get_all_values(int id, int mode, int measureCount);

/**
  * 舵机角度查询
  *
  * @param id 舵机编号
  */
extern int uc_servo_get_angle(int id);

/**
 *  舵机角度控制
 *  @param id 舵机编号
 *  @param angle 角度
 */
extern void uc_servo_angle_ctrl(int id, int angle);

/**
  * 舵机角度成组控制
  *
  * @param a1 1号舵机
  * @param a2 2号舵机
  * @param a3 3号舵机
  * @param a4 4号舵机
  */
extern void uc_servo_angle_group_ctrl(int a1, int a2, int a3, int a4);

/** 获得马达转圈数 */
extern int uc_motor_get_count(int id);

/** 重置马达转圈数 */
extern void uc_motor_reset_count(int id);

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
extern void uc_motor_power_single_ctrl(int id, int power, int dis, int angle);

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
extern void uc_motor_power_group_ctrl(int* param1, int* param2, int* param3, int* param4);

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
extern void uc_motor_speed_single_ctrl(int id, int speed, int dis, int angle);

/**
  * 电机速度、舵机角度成组控制
  * 参数定义：array[0]:速度; array[1]:行走距离; array[2]:舵机角度
  *
  * @param param1 1号舵机设置参数
  * @param param2 2号舵机设置参数
  * @param param3 3号舵机设置参数
  * @param param4 4号舵机设置参数
  */
extern void uc_motor_speed_group_ctrl(int* param1, int* param2, int* param3, int* param4);

/** 设置马达速度 */
extern void uc_motor_set_speed(int id, int power, int mode);

/** 用于履带车控制马达 */
extern void uc_motors_set_speed(int power1, int mode1, int dis1, int power2, int mode2, int dis2);

extern void uc_servo_motor_ctrl(int angle, int power, int mode, int dis);

#endif // _SYSCOM_H

