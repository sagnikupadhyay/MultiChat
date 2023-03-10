package Client;

import java.io.IOException;
import java.util.Scanner;

public class ClientManager
{
    public static void StartClient(ClientData clientData)
    {
        //This method starts the client.
        SendMessage(clientData);
        ReceiveMessage(clientData);
    }

    public static void StopClient(ClientData clientData)
    {
        //This method stops the client.
        try {
            if(clientData.Reader != null)
                clientData.Reader.close();
            if(clientData.Writer != null)
                clientData.Writer.close();
            if(clientData.ClientSocket != null)
                clientData.ClientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void SendMessage(ClientData clientData)
    {
        //This method sends the username at first and then the user message to the server,
        // which then broadcasts the message to all other clients.
        Thread sendThread = new Thread(() ->
        {
            try {
                //Send the username to the server
                clientData.Writer.write(clientData.UserName);
                clientData.Writer.newLine();
                clientData.Writer.flush();

                //Send the user message to the server
                Scanner scanner = new Scanner(System.in);
                while (clientData.ClientSocket.isConnected())
                {
                    String sendMsgToServer = scanner.nextLine();
                    clientData.Writer.write(clientData.UserName + ": " + sendMsgToServer);
                    clientData.Writer.newLine();
                    clientData.Writer.flush();
                }
                //Close scanner and stop client when socket connection is lost
                scanner.close();
                StopClient(clientData);
            }
            catch (IOException e) {
                //Stop the client if we run into an error
                StopClient(clientData);
            }
        });
        sendThread.start();
    }

    public static void ReceiveMessage(ClientData clientData)
    {
        //This method receives the message that has been broadcast by the server to the clients
        // [except the client that sent the message to the server.]
        Thread receiveThread = new Thread(() ->
        {
            while(clientData.ClientSocket.isConnected())
            {
                try {
                    //Receive message from the server
                    String receiveMsgFromServer = clientData.Reader.readLine();
                    System.out.println(receiveMsgFromServer);
                }
                catch (IOException e) {
                    //Stop the client if we run into an error
                    StopClient(clientData);
                    break;
                }
            }
        });
        receiveThread.start();
    }
}
