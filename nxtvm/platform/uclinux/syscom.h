#ifndef _SYSCOM_H
#define _SYSCOM_H

#include     "classes.h"
#include     "memory.h"

/** �򿪴����豸 */
extern void openDev();

/** ���ݶ˿ںŻ�ô���������
 *
 *  @param id ���������
 *
 *  @param mode ����ģʽ<br>
 *              0:�������ݲ�������,ֱ�ӷ���<br>
 *              1:�޳����������е������Сֵ��ʣ�����ݷ���<br>
 *              2:����������ȡƽ���󷵻�ƽ��ֵ<br>
 *              4:�޳����������е������Сֵ��ƽ��ֵ����
 *
 *  @param measureCount ��������,��Χ1-7;��ֵС��4ʱ,�ù���ģʽΪ0
 *
 */
extern Object * uc_sensor_get_all_values(int id, int mode, int measureCount);

/**
  * ����ǶȲ�ѯ
  *
  * @param id ������
  */
extern int uc_servo_get_angle(int id);

/**
 *  ����Ƕȿ���
 *  @param id ������
 *  @param angle �Ƕ�
 */
extern void uc_servo_angle_ctrl(int id, int angle);

/**
  * ����Ƕȳ������
  *
  * @param a1 1�Ŷ��
  * @param a2 2�Ŷ��
  * @param a3 3�Ŷ��
  * @param a4 4�Ŷ��
  */
extern void uc_servo_angle_group_ctrl(int a1, int a2, int a3, int a4);

/** ������תȦ�� */
extern int uc_motor_get_count(int id);

/** �������תȦ�� */
extern void uc_motor_reset_count(int id);

/**
  * ��������ʡ�����Ƕȿ���
  *
  * @param id
  *            ������
  * @param power
  *            ����(ǰ����1-100���� >100����) (���ˣ�-1 - -100���� <-100����) (0ɲ��)
  * @param dis
  *            ���߾���>=0, 0һֱ����
  * @param angle
  *            ����Ƕ�0-1800
  */
extern void uc_motor_power_single_ctrl(int id, int power, int dis, int angle);

/**
  * ������ʡ�����Ƕȳ������
  * �������壺
  *     array[0]:���� (ǰ����1-100���� >100����) (���ˣ�-1 - -100���� <-100����) (0ɲ��)
  *     array[1]:���߾���>=0, 0һֱ����
  *     array[2]:����Ƕ�0-1800
  *
  * @param param1 1�Ŷ�����ò���
  * @param param2 2�Ŷ�����ò���
  * @param param3 3�Ŷ�����ò���
  * @param param4 4�Ŷ�����ò���
  */
extern void uc_motor_power_group_ctrl(int* param1, int* param2, int* param3, int* param4);

/**
  * ��������ʡ�����Ƕȿ���
  *
  * @param id
  *            ������
  * @param speed
  *            �ٶ�: >0ǰ��, <0����, ==0ɲ��
  * @param dis
  *            ���߾���>=0, 0һֱ����
  * @param angle
  *            ����Ƕ�0-1800
  */
extern void uc_motor_speed_single_ctrl(int id, int speed, int dis, int angle);

/**
  * ����ٶȡ�����Ƕȳ������
  * �������壺array[0]:�ٶ�; array[1]:���߾���; array[2]:����Ƕ�
  *
  * @param param1 1�Ŷ�����ò���
  * @param param2 2�Ŷ�����ò���
  * @param param3 3�Ŷ�����ò���
  * @param param4 4�Ŷ�����ò���
  */
extern void uc_motor_speed_group_ctrl(int* param1, int* param2, int* param3, int* param4);

/** ��������ٶ� */
extern void uc_motor_set_speed(int id, int power, int mode);

/** �����Ĵ���������� */
extern void uc_motors_set_speed(int power1, int mode1, int dis1, int power2, int mode2, int dis2);

extern void uc_servo_motor_ctrl(int angle, int power, int mode, int dis);

#endif // _SYSCOM_H

