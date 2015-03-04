//server
import java.net.*;
import java.io.*;
import java.util.Vector;
import java.lang.*;



public class ChatServer {

    private static int port = 1234;
    private static ServerSocket server = null;
    private static Socket clientSocket;
    private static String line;
    private static BufferedReader streamIn;
    private static PrintStream streamOut;
    private static Vector<String> usernames = new Vector<String>();
    private static Vector<PrintStream> streams = new Vector<PrintStream>();

    public static void main(String[] args) throws IOException{

        try{
            System.out.println("Connecting to port " + port + " ....");
            server = new ServerSocket(port);
            System.out.println("Chat application server is now running..");
            while(true){
                clientSocket = server.accept();
                chatHandler c = new chatHandler(clientSocket);
                c.start();
            }
        }
        catch(IOException e){
            System.out.println("Couldn't connect to the port!");
        }
        finally{
            server.close();
        }
    }


    private static class chatHandler extends Thread{

        private Socket clientSocket;

        public chatHandler(Socket clientSocket){
            super("chatHandler");
            this.clientSocket = clientSocket;
        }

        public void run(){
            try{
                streamIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                streamOut = new PrintStream(clientSocket.getOutputStream(), true);

                while(true){
                    streamOut.println("Username");
                    String line = streamIn.readLine();
                    if (line == null){
                        return;
                    }
                    try{
                        synchronized(usernames){
                            if(!usernames.contains(line)){
                                usernames.add(line);
                                break;
                            }
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
                streamOut.println("Welcome");
                streams.add(streamOut);

                while(true){
                    String message = streamIn.readLine();
                    if(message == null){
                        return;
                    }
                    for(PrintStream stream : streams){
                        stream.println("From " + line + ": " + message);
                    }
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
            finally{
                if(line != null && streamOut != null){
                    usernames.remove(line);
                    streams.remove(streamOut);
                }
                try{
                    clientSocket.close();
                }
                catch(IOException e){
                    System.out.println(e);
                }
            }
        }
    }
}
