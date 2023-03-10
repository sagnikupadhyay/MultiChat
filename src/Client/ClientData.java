package Client;

import java.io.*;
import java.net.Socket;

public class ClientData
{
    public Socket ClientSocket;
    public BufferedReader Reader;
    public BufferedWriter Writer;
    public String UserName;

    public ClientData(Socket clientSocket, String userName) throws IOException
    {
        ClientSocket = clientSocket;
        Reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        UserName = userName;
    }
}
