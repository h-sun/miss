#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <dirent.h>
#include "types.h"

//�ļ���һ������������ļ���ɾ���ļ��������ļ���
extern int file_general_operation(char* fname, int mode);

//�ļ��Ĳ�ѯ�������ļ��Ƿ���ڣ��Ƿ�ɶ����Ƿ��д���Ƿ����أ��ļ�����
extern int file_query_operation(char* fname, int mode);

//�ļ���д���������ǣ�׷��
extern int file_write_operation(char* fname, char* str, int mode);

//�ļ��Ķ����������У����ֽ�
extern int file_read_operation(char* fname, char *buf, int length, int mode, int param);

extern int file_specific_query_operation(char* fname, char* buf, int len, int num);
