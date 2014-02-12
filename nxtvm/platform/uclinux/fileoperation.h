#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <dirent.h>
#include "types.h"

//文件的一般操作：创建文件，删除文件，创建文件夹
extern int file_general_operation(char* fname, int mode);

//文件的查询操作：文件是否存在，是否可读，是否可写，是否隐藏，文件行数
extern int file_query_operation(char* fname, int mode);

//文件的写操作：覆盖，追加
extern int file_write_operation(char* fname, char* str, int mode);

//文件的读操作：按行，按字节
extern int file_read_operation(char* fname, char *buf, int length, int mode, int param);

extern int file_specific_query_operation(char* fname, char* buf, int len, int num);
