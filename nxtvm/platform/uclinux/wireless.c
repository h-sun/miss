#include <wireless.h>

int sockfd;

//return 1:socket error;  2:connect error;  0:successful
int wifi_connect_server(char *serverIp, int serverPort)
{
    struct sockaddr_in server_addr;
    if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        return 1;
    }
    memset(&server_addr, 0, sizeof(struct sockaddr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(serverPort);
    server_addr.sin_addr.s_addr = inet_addr(serverIp);
    if(connect(sockfd, (struct sockaddr *)&server_addr, sizeof(struct sockaddr)) == -1)
    {
        return 2;
    }

    return 0;
}

void wifi_disconnect_server()
{
    close(sockfd);
}

//if return -1, send failed
int wifi_send_to_server(char *buf, int len)
{
    return send(sockfd, buf, len, 0);
}

//if return -1, receive failed
int wifi_receive_from_server(char *buf, int len)
{
    return recv(sockfd, buf, len, 0);
}

