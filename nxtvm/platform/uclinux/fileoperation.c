#include "fileoperation.h"


#define OPER_GEN_CREATE 1
#define OPER_GEN_DELETE 2
#define OPER_GEN_MKDIRS 3

#define OPER_QUERY_EXIST 11
#define OPER_QUERY_READ 12
#define OPER_QUERY_WRITE 13
#define OPER_QUERY_HIDDEN 14
#define OPER_QUERY_FOLDER 15
#define OPER_QUERY_LINE_NUMS 16

#define OPER_IO_W_COVER 21
#define OPER_IO_W_APPEND 22

#define FILE_SEPARATOR "/"

#define TRUE 1
#define FALSE 0
#define boolean int

typedef struct{
     int number;        /*分割的字符串个数*/
     char** string;        /*字符串数组*/
}StringTab;

//从完整路径提取出各个文件夹的名称
StringTab StringSplit_Struct(char* string){
    StringTab result;
    /*首先分配一个char*的内存，然后再动态分配剩下的内存*/
    result.string = (char * * )malloc(sizeof(char *)*1);
    memset(result.string,0,sizeof(char *)*1);
    /*无论是否存在该分割串，绝对都会分割到一个字符串*/
    result.number = 0;
    /*定义一个遍历用的指针和一个寻找位置用的指针*/
    char* p = string;
    char* pos = string;
    char* temp;

    while(p[0] != '\0') {
        if (p[0] == '/') {
            //字符串首字符为'/'
            if ( pos[0] == '/') {
                result.string = (char * * )realloc(result.string,sizeof(char *)*(1));
                result.string[result.number] = "/";
                result.number++;

                while (p[0]=='/'){
                    p++;
                }
                pos = p;
                continue;
            }
            /*分配临时字符串空间*/
            int n = p-pos+1;
            temp = (char * )malloc(sizeof(char)*n);
            int i=0;
            while(pos<p) {
                temp[i++] = pos[0];
                pos++;
            }
            /*将字符串结尾置零*/
            temp[i] = '\0';
            result.string = (char * * )realloc(result.string,sizeof(char *)*(result.number+1));
            result.string[result.number] = temp;
            /*计数器加一*/
            result.number++;
            while (p[0]=='/'){
                p++;
            }
            pos = p;
            continue;
        }

        p++;
    }

    if(pos[0]!='\0'){
        /*分配临时字符串空间*/
        int n = p-pos+1;
        temp = (char * )malloc(sizeof(char)*n);
        int i=0;
        while(pos<p) {
            temp[i++] = pos[0];
            pos++;
        }
        /*将字符串结尾置零*/
        temp[i] = '\0';
        result.string = (char * * )realloc(result.string,sizeof(char *)*(result.number+1));
        result.string[result.number] = temp;
        /*计数器加一*/
        result.number++;
     }

     return result;
}


//判断文件是否存在
boolean file_is_exist(char* fname) {
    if (!access(fname,0)) {
        return TRUE;
    }
    return FALSE;
}

//判断是否为文件夹
boolean file_is_folder(char* path) {
    int i = chdir(path);
    if (i==0){
        return TRUE;
    }
    return FALSE;
}

//判断文件是否可写
boolean file_can_write(char* fname) {
    if (!access(fname,2)) {
        return TRUE;
    }
    return FALSE;
}

//判断文件是否可读
boolean file_can_read(char* fname) {
    if (!access(fname,4)) {
        return TRUE;
    }
    return FALSE;
}

//创建文件夹
boolean file_mkdirs(char* path) {
    StringTab array;
    int i;
    boolean b = TRUE;
    array = StringSplit_Struct(path);

    for(i=0;i<array.number;i++) {
        printf("mkdir:%s\n", array.string[i]);
        if (file_is_exist(array.string[i]) == FALSE) {
            if(mkdir(array.string[i]) != 0) {
                b = FALSE;
                break;
            }
        }
        if(chdir(array.string[i])!=0){
            b = FALSE;
            break;
        }
    }
    return b;
}

//创建新文件
boolean file_create_new_file(char* fname) {
    FILE *fp = NULL;
    long fSet = 0, fEnd = 0;
    long filelen = 0;
    boolean b;

    if (file_is_exist(fname) == TRUE) {
        return FALSE;
    }

    if( (fp = fopen(fname,"w+")) == NULL ){
        b = FALSE;
    }else{
        b = TRUE;
    }

    fclose(fp);
    return b;
}

//删除文件
boolean file_delete_file(char* fname){
    if(remove(fname)==-1)
        return FALSE;
    else
        return TRUE;
}

//获得文件的字节长度
long file_lenth(char* fname){
    FILE *fp = NULL;
    long fSet = 0, fEnd = 0;
    long filelen = 0;
    //fname = "d:/test.txt"

    if( (fp = fopen(fname,"r+")) == NULL ){
        printf( "The file '%s' was not opened\n", fname );
        fclose(fp);
        return -1;
    }else{
        fseek(fp,0,SEEK_SET);
        fSet = ftell(fp);

        fseek(fp,0,SEEK_END);
        fEnd = ftell(fp);

        /* file lenght */
        filelen = fEnd - fSet ;

        fclose(fp);
        return filelen;
    }
}

//获得文件的总行号
int file_get_line_num(char* fname){
    FILE *fp;
    char *buf, *p;
    int index = 0;
    if ((fp=fopen(fname, "r"))==NULL) {
        printf("open file error!!\n");
        return -1;
    }

    buf=(char*)malloc(1024*sizeof(char));
    while(1) {
        p=fgets(buf, 1024, fp);
        if (!p) {
            break;
        }
        index++;
    }

    fclose(fp);

    return index;
}

//以覆盖的方式写入文件
boolean file_write_file(char* fname, char* str) {
    FILE *fp = fopen(fname, "w+");
    int ret = -1;
    boolean b;
    ret = fprintf(fp, "%s\n", str);

    if(ret >= 0){
        fflush(fp);
        b = TRUE;
    }
    else {
        b = FALSE;
    }
    fclose(fp);

    return b;
}

//以追加的方式写入文件
boolean file_append_file(char* fname, char* str) {
    FILE *fp = fopen(fname, "a+");
    int ret = -1;
    boolean b;
    ret = fprintf(fp, "%s", str);

    if(ret >= 0){
        fflush(fp);
        b = TRUE;
    }
    else {
        b = FALSE;
    }
    fclose(fp);

    return b;
}

//文件的一般操作：创建文件，删除文件，创建文件夹
boolean file_general_operation(char* fname, int mode) {
    boolean b = FALSE;
    switch (mode) {
        case OPER_GEN_CREATE :
            b = file_create_new_file(fname);
            break;
        case OPER_GEN_DELETE :
            b = file_delete_file(fname);
            break;
        case OPER_GEN_MKDIRS :
            b = file_mkdirs(fname);
            break;
        default :
            b = FALSE;
            break;
    }

    return b;
}

//文件的查询操作：文件是否存在，是否可读，是否可写，是否隐藏，文件行数
int file_query_operation(char* fname, int mode) {
    int b = 0;
    switch (mode) {
        case OPER_QUERY_EXIST :
            b = file_is_exist(fname);
            break;
        case OPER_QUERY_READ :
            b = file_can_read(fname);
            break;
        case OPER_QUERY_WRITE :
            b = file_can_write(fname);
            break;
        case OPER_QUERY_HIDDEN :
            if(fname[0]=='.')
                b = TRUE;
            break;
        case OPER_QUERY_LINE_NUMS :
            b = file_get_line_num(fname);
            break;
        case OPER_QUERY_FOLDER :
            b = file_is_folder(fname);
            break;
        default :
            b = 0;
            break;
    }

    return b;
}

//文件的写操作：覆盖，追加
boolean file_write_operation(char* fname, char* str, int mode) {
    boolean b = FALSE;
    switch (mode) {
        case OPER_IO_W_COVER :
            b = file_write_file(fname, str);
            break;
        case OPER_IO_W_APPEND :
            b = file_append_file(fname, str);
            break;
        default :
            break;
    }
    return b;
}

//读取文件
int file_read_operation(char* fname, char *buf, int length, int mode, int param) {
    int i = -1;
    switch (mode) {
        case 1:
            i = file_read_line(fname, buf, length, param);
            break;
        case 2:
            i = file_read_bytes(fname, buf, length, param);
            break;
        default :
            break;
    }

    return i;
}

//读取文件的一定字符
int file_read_bytes(char* fname, char *buf, int length, int offset) {
    FILE *fp;
    char *p;
    int i=0;
    if ((fp=fopen(fname, "r"))==NULL) {
        printf("open file error!!\n");
        return 0;
    }
    fseek(fp,offset,0);
    char c;

    while (!feof(fp)) { //判定文件是否结尾
        buf[i++] = fgetc(fp);
        if(i==length) {
            break;
        }
    }

    fclose(fp);

    return i>0?i-1:0;//不好的处理方案，待解决
}

//读取文件的行字符串
int file_read_line(char* fname, char *buf, int length, int lineNum) {
    FILE *fp;
    char *p;
    int i=0, index = 1;
    if ((fp=fopen(fname, "r"))==NULL) {
        printf("open file error!!\n");
        return 0;
    }
    while(1) {
        p=fgets(buf, length, fp);
        if(!p) {
            memset(buf, '\0', 1);
            i=-1;
            break;
        }
        if (index == lineNum) {
            break;
        }
        index++;
    }

    fclose(fp);

    if(i==-1){
        return i;
    }

    for(i=0; i<length; i++){
        if (buf[i] == '\0') {
            break;
        }
    }
    //i = i>0?i-1:i;

    return i;
}

int file_specific_query_operation(char* fname, char* buf, int len, int num) {
    DIR* dirp;
    struct dirent* direntp;
    dirp = opendir(fname);
    int i=0;
    if(dirp != NULL) {
        int index=1;
        while(1){
            direntp = readdir(dirp);
            if( direntp == NULL ){
                break;
            }
            char* dname = direntp->d_name;
            if(dname[0]=='.' && dname[1]=='\0'){
                continue;
            }
            if(dname[0]=='.' && dname[1]=='.' && dname[2]=='\0'){
                continue;
            }
            if (index < num){
                index++;
                continue;
            }
            while(dname[i]!='\0' && i<len){
                buf[i]=dname[i];
                i++;
            }
            break;
        }
        closedir(dirp);
    }

    return i;
}
