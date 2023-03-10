package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerManager
{
    public static void StartServer(ServerData serverData) {
        //This method starts the server.
        System.out.println("SERVER started.");
        try {
            while(!serverData.ServerSocket.isClosed()) {
                System.out.println("Waiting for clients...");
                Socket clientSocket = serverData.ServerSocket.accept();
                GhostClientData ghostClient = new GhostClientData(clientSocket);

                RegisterClient(ghostClient);

                Thread broadcastThread = new Thread(() -> {
                    while(ghostClient.ClientSocket.isConnected()) {
                        try {
                            String msgFromClient = ghostClient.Reader.readLine();
                            ServerManager.BroadcastMessage(ghostClient, msgFromClient, ServerData.ghostClients);
                        }
                        catch (IOException e) {
                            DestroyGhostClient(ghostClient);
                            DeRegisterClient(ghostClient);
                            break;
                        }
                    }
                });
                broadcastThread.start();
            }
            StopServer(serverData);
        }
        catch (IOException e) {
            StopServer(serverData);
        }
    }

    public static void StopServer(ServerData serverData) {
        //This method stops the server.
        System.out.println("SERVER stopped.");
        try {
            if(serverData.ServerSocket != null)
                serverData.ServerSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RegisterClient(GhostClientData clientHandler) {
        //Register client in server database.
        ServerData.ghostClients.add(clientHandler);
        System.out.println("A new client [" + clientHandler.ClientUserName +"] has connected to the server.");
        BroadcastMessage(clientHandler, "SERVER: " + clientHandler.ClientUserName + " has joined the chat.", ServerData.ghostClients);
    }

    private static void DeRegisterClient(GhostClientData ghostClient) {
        //DeRegister client in server database.
        ServerData.ghostClients.remove(ghostClient);
        System.out.println("A client [" + ghostClient.ClientUserName +"] has disconnected from the server.");
        BroadcastMessage(ghostClient, "SERVER: " + ghostClient.ClientUserName + " has left the chat.", ServerData.ghostClients);
    }

    public static void BroadcastMessage(GhostClientData sender, String messageToSend, ArrayList<GhostClientData> receivers) {
        //Broadcast message to clients
        for(GhostClientData clientHandler : receivers) {
            try {
                if(!clientHandler.ClientUserName.equals(sender.ClientUserName)) {
                    clientHandler.Writer.write(messageToSend);
                    clientHandler.Writer.newLine();
                    clientHandler.Writer.flush();
                }
            }
            catch (IOException e) {
                DestroyGhostClient(clientHandler);
            }
        }
    }

    public static void DestroyGhostClient(GhostClientData ghostClient) {
        //This method stops the client.
        try {
            if(ghostClient.Reader != null)
                ghostClient.Reader.close();
            if(ghostClient.Writer != null)
                ghostClient.Writer.close();
            if(ghostClient.ClientSocket != null)
                ghostClient.ClientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
