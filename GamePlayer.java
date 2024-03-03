import java.io.*;
import java.net.*;

public class GamePlayer {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int portNumber = 12345; // Ensure the port number matches in the Server program

        try (Socket socket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String fromServer;
            int messageCount = 1;
            while ((fromServer = in.readLine()) != null) {
                // Server requests a string
                System.out.println("Server: " + fromServer);
                // Client sends a response
                String message = "Client Message " + messageCount++;
                out.println(message);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
