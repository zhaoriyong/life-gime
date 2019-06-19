import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server {
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;
    public static void main(String[] args){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("服务端");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBounds(300, 200, 500, 300);
        frame.setLayout(new BorderLayout(10, 0));
 
        JPanel northPanel = new JPanel();
        JLabel portLabel = new JLabel("Port: ");
        JTextField portField = new JTextField(28);
        northPanel.setBorder(BorderFactory.createTitledBorder("服务器设置: "));
        JButton startBtn = new JButton("Start");
        northPanel.add(portLabel);
        northPanel.add(portField);
        northPanel.add(startBtn);
 
        JPanel centerPanel = new JPanel();
        JTextArea area = new JTextArea(10, 35);
        area.setMargin(new Insets(10, 10, 10, 10));
        area.setEnabled(false);
        centerPanel.add(area);
 
        JPanel southPanel = new JPanel();
        JLabel sayLabel = new JLabel("Say: ");
        JTextField sayField = new JTextField(30);
        JButton sayBtn = new JButton("Say");
        southPanel.add(sayLabel);
        southPanel.add(sayField);
        southPanel.add(sayBtn);
 
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
 
        frame.setVisible(true);
 
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.append("Server starting…\n");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            serverSocket = new ServerSocket(Integer.parseInt(portField.getText()));
                            socket = serverSocket.accept();
                            area.append("Client connected…\n");
                            while(true){
                                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                String res = null;
                                while((res = br.readLine()) != null){
                                    area.append(res + "\n");
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });
 
        sayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    pw.println(sayField.getText());
                    pw.flush();
                    area.append(sayField.getText() + "\n");
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }
}

