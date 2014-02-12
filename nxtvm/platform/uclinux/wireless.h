#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

//return 1:socket error;  2:connect error;  0:successful
extern int wifi_connect_server(char *serverIp, int serverPort);

extern void wifi_disconnect_server();

//if return -1, send failed
extern int wifi_send_to_server(char *buf, int len);

//if return -1, receive failed
extern int wifi_receive_from_server(char *buf, int len);
