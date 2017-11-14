import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
    public static void main(String[] args) throws IOException {
        final ServerSocket commandSocket = new ServerSocket(2121);
        final ServerSocket dataSocket = new ServerSocket(2020);

        try {
            for (;;) {
                final Socket clientCommand = commandSocket.accept();

                System.out.println("Accepted connection from " + commandSocket);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (
                            BufferedReader br = new BufferedReader(
                                new InputStreamReader(clientCommand.getInputStream())
                            );
                            PrintWriter out = new PrintWriter(clientCommand.getOutputStream(), true)
                        ) {
                            final Socket clientData = dataSocket.accept();
                            System.out.println("Accepted data connection from " + dataSocket);

                            String commandInput;
                            while ((commandInput = br.readLine()) != null) {
                                System.out.println("Client command : "  + commandInput);
                                out.println(commandInput);
                                System.out.println("send response");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            try { clientCommand.close(); } catch (IOException ex) { }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}