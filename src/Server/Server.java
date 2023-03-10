package Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(1234);
        serverSocket.setReuseAddress(true);
        ServerData serverData = new ServerData(serverSocket);

        ServerManager.StartServer(serverData);
    }
}
