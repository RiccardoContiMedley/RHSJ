import java.io.*;
import java.net.*;

public class GameServer {
    public static void main(String[] args) {
        int portNumber = 12345; // Ensure the port number matches in both clients

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server started. Waiting for the first client...");
            Socket clientSocket1 = serverSocket.accept();
            System.out.println("First client connected.");

            System.out.println("Waiting for the second client...");
            Socket clientSocket2 = serverSocket.accept();
            System.out.println("Second client connected.");

            try (PrintWriter out1 = new PrintWriter(clientSocket1.getOutputStream(), true);
                 BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
                 PrintWriter out2 = new PrintWriter(clientSocket2.getOutputStream(), true);
                 BufferedReader in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()))) {
                 
                while (true) {
                    // Request and receive a string from the first client
                    out1.println("Server Request: Send a new string");
                    String response1 = in1.readLine();
                    if (response1 != null) {
                        System.out.println("Received from client 1: " + response1);
                    } else {
                        break; // End the loop if the connection is closed
                    }

                    // Wait 2 seconds before asking the next client
                    Thread.sleep(2000);

                    // Request and receive a string from the second client
                    out2.println("Server Request: Send a new string");
                    String response2 = in2.readLine();
                    if (response2 != null) {
                        System.out.println("Received from client 2: " + response2);
                    } else {
                        break; // End the loop if the connection is closed
                    }

                    // Wait 2 seconds before asking the first client again
                    Thread.sleep(2000);
                }
            } finally {
                clientSocket1.close();
                clientSocket2.close();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Server was interrupted.");
        }
    }
}
