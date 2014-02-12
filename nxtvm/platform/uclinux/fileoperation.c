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
     int number;        /*�ָ���ַ�������*/
     char** string;        /*�ַ�������*/
}StringTab;

//������·����ȡ�������ļ��е�����
StringTab StringSplit_Struct(char* string){
    StringTab result;
    /*���ȷ���һ��char*���ڴ棬Ȼ���ٶ�̬����ʣ�µ��ڴ�*/
    result.string = (char * * )malloc(sizeof(char *)*1);
    memset(result.string,0,sizeof(char *)*1);
    /*�����Ƿ���ڸ÷ָ�����Զ���ָһ���ַ���*/
    result.number = 0;
    /*����һ�������õ�ָ���һ��Ѱ��λ���õ�ָ��*/
    char* p = string;
    char* pos = string;
    char* temp;

    while(p[0] != '\0') {
        if (p[0] == '/') {
            //�ַ������ַ�Ϊ'/'
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
            /*������ʱ�ַ����ռ�*/
            int n = p-pos+1;
            temp = (char * )malloc(sizeof(char)*n);
            int i=0;
            while(pos<p) {
                temp[i++] = pos[0];
                pos++;
            }
            /*���ַ�����β����*/
            temp[i] = '\0';
            result.string = (char * * )realloc(result.string,sizeof(char *)*(result.number+1));
            result.string[result.number] = temp;
            /*��������һ*/
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
        /*������ʱ�ַ����ռ�*/
        int n = p-pos+1;
        temp = (char * )malloc(sizeof(char)*n);
        int i=0;
        while(pos<p) {
            temp[i++] = pos[0];
            pos++;
        }
        /*���ַ�����β����*/
        temp[i] = '\0';
        result.string = (char * * )realloc(result.string,sizeof(char *)*(result.number+1));
        result.string[result.number] = temp;
        /*��������һ*/
        result.number++;
     }

     return result;
}


//�ж��ļ��Ƿ����
boolean file_is_exist(char* fname) {
    if (!access(fname,0)) {
        return TRUE;
    }
    return FALSE;
}

//�ж��Ƿ�Ϊ�ļ���
boolean file_is_folder(char* path) {
    int i = chdir(path);
    if (i==0){
        return TRUE;
    }
    return FALSE;
}

//�ж��ļ��Ƿ��д
boolean file_can_write(char* fname) {
    if (!access(fname,2)) {
        return TRUE;
    }
    return FALSE;
}

//�ж��ļ��Ƿ�ɶ�
boolean file_can_read(char* fname) {
    if (!access(fname,4)) {
        return TRUE;
    }
    return FALSE;
}

//�����ļ���
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

//�������ļ�
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

//ɾ���ļ�
boolean file_delete_file(char* fname){
    if(remove(fname)==-1)
        return FALSE;
    else
        return TRUE;
}

//����ļ����ֽڳ���
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

//����ļ������к�
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

//�Ը��ǵķ�ʽд���ļ�
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

//��׷�ӵķ�ʽд���ļ�
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

//�ļ���һ������������ļ���ɾ���ļ��������ļ���
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

//�ļ��Ĳ�ѯ�������ļ��Ƿ���ڣ��Ƿ�ɶ����Ƿ��д���Ƿ����أ��ļ�����
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

//�ļ���д���������ǣ�׷��
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

//��ȡ�ļ�
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

//��ȡ�ļ���һ���ַ�
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

    while (!feof(fp)) { //�ж��ļ��Ƿ��β
        buf[i++] = fgetc(fp);
        if(i==length) {
            break;
        }
    }

    fclose(fp);

    return i>0?i-1:0;//���õĴ������������
}

//��ȡ�ļ������ַ���
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
