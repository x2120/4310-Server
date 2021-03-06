//y u no comment
//you're a commentless monster
//this is torturous
import java.net.*;
import java.io.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.event.*;

public class ChatClient {

    //
    private static int port = 1234;
    JFrame window = new JFrame("Chat Group");
    JButton sendBox = new JButton("Send Message");
    JTextField inputMsg = new JTextField(35);
    JTextArea outputMsg = new JTextArea(10, 35);
    private static BufferedReader streamIn;
    private static PrintStream streamOut;

    //
    public static void main(String[] args) throws Exception{
        ChatClient client = new ChatClient();
        client.window.setVisible(true);
        client.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.run();
    }

    //
    public ChatClient(){
        outputMsg.setSize(40, 20);
        inputMsg.setSize(5, 10);
        sendBox.setSize(35,50);
        inputMsg.setEditable(false);
        outputMsg.setEditable(false);
        window.getContentPane().add(inputMsg, "South");
        window.getContentPane().add(outputMsg, "East");
        window.getContentPane().add(sendBox, "West");
        window.pack();
        sendBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                streamOut.println(inputMsg.getText());
                inputMsg.setText("");
            }
        });

        //
        inputMsg.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                streamOut.println(inputMsg.getText());
                inputMsg.setText("");
            }
        });
    }

    //gets username
    private String getUsername(){
        return JOptionPane.showInputDialog(window, "Client Username:", "Welcome to Chat", JOptionPane.QUESTION_MESSAGE);
    }

    //gets destination username
    private String getDestination(){
        return JOptionPane.showInputDialog(window, "Destination Username:", "Welcome to Chat", JOptionPane.QUESTION_MESSAGE);
    }

    //
    private void run() throws IOException{
        Socket clientSocket = new Socket("localhost", port);
        streamIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        streamOut = new PrintStream(clientSocket.getOutputStream(), true);

        while(true){
            String line = streamIn.readLine();
            if(line.startsWith("Username")){
                streamOut.println(getUsername());
            }
            else if(line.startsWith("Destination")){
                streamOut.println(getDestination());
            }
            else if(line.startsWith("Welcome")){
                inputMsg.setEditable(true);
            }
            else if(line.startsWith("From")){
                outputMsg.append(line.substring(10)+ "\n");
            }
        }
    }
}
