import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
 
public class Client {
    private static Socket socket = null;
    public static void main(String[] args){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("客户端");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBounds(300, 200, 500, 300);
        frame.setLayout(new BorderLayout(10, 0));
 
        JPanel northPanel = new JPanel();
        JLabel ipLabel = new JLabel("Server Ip: ");
        JTextField ipField = new JTextField(10);
        JLabel portLabel = new JLabel("Server Port: ");
        JTextField portField = new JTextField(6);
        northPanel.setBorder(BorderFactory.createTitledBorder("客户机设置: "));
        JButton connectBtn = new JButton("Connect");
        northPanel.add(ipLabel);
        northPanel.add(ipField);
        northPanel.add(portLabel);
        northPanel.add(portField);
        northPanel.add(connectBtn);
 
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
 
        connectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area.append("Connect to server…\n");
                try {
                    socket = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
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
 

