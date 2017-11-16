import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
    public static void main(String[] args) throws IOException {
        final ServerSocket c_socket = new ServerSocket(2121);
        final ServerSocket d_socket = new ServerSocket(2020);

        System.out.println("Server waiting connection on Port 2121, 2020");

        try {
            for (;;) {
                final Socket c_client = c_socket.accept();

                System.out.println("Accepted connection from " + c_socket);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try (
                            BufferedReader br = new BufferedReader(
                                new InputStreamReader(c_client.getInputStream())
                            );
                            PrintWriter out = new PrintWriter(c_client.getOutputStream(), true)
                        ) {
                            final Socket d_client = d_socket.accept();

                            String commandInput;
                            String[] commandArray;
                            while ((commandInput = br.readLine()) != null) {
                                commandArray = commandInput.split(" ");
                                String command = commandArray[0];
                                String c_arg = (commandArray.length > 1) ? commandArray[1] : "undefined";

                                if (command.compareTo("LIST") == 0 || command.compareTo("ls") == 0) {
                                    System.out.println("LIST command, argument :" + c_arg);

                                } else if (command.compareTo("CD") == 0 || command.compareTo("cd") == 0) {
                                    System.out.println("CD command, argument :" + c_arg);
                                } else if (command.compareTo("GET") == 0) {
                                    System.out.println("GET command, argument :" + c_arg);
                                } else if (command.compareTo("PUT") == 0) {
                                    System.out.println("PUT command, argument :" + c_arg);
                                } else {
                                    out.println(command + ": command not found");
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            try { c_client.close(); } catch (IOException ex) { }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ListCommand(String arg, PrintWriter out) throws Exception {
        if (arg.compareTo("undefined") == 0) {
            out.println("need argument");
        }
    }
}