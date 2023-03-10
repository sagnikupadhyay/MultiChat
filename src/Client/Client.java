package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        ClientData clientData = new ClientData(socket, userName);

        ClientManager.StartClient(clientData);
    }
}
