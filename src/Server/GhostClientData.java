package Server;

import java.io.*;
import java.net.Socket;

public class GhostClientData
{
    public Socket ClientSocket;
    public BufferedReader Reader;
    public BufferedWriter Writer;
    public String ClientUserName;

    public GhostClientData(Socket clientSocket) throws IOException
    {
        ClientSocket = clientSocket;
        Reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        Writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        ClientUserName = Reader.readLine();
    }
}
