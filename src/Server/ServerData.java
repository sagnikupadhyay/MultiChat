package Server;

import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerData
{
    public static ArrayList<GhostClientData> ghostClients = new ArrayList<>();

    public ServerSocket ServerSocket;
    public ServerData(ServerSocket serverSocket)
    {
        ServerSocket = serverSocket;
    }
}
