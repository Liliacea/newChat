import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Client extends JFrame implements ActionListener, TCPConnection {


    private final String IP = "127.0.0.1";
    private final int PORT = 10001;
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private Network connection;




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });

    }
    private JTextArea log = new JTextArea();
    private JTextField fieldNickName = new JTextField();
    private JTextField fieldMessage = new JTextField();

    public Client() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setVisible(true);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        log.setEditable(false);
        fieldMessage.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(fieldNickName, BorderLayout.NORTH);
        add(fieldMessage, BorderLayout.SOUTH);
        {
            try {
                connection = new Network(this,IP,PORT);
            } catch (IOException e) {
                e.printStackTrace();
                printMessage("TCPConnection exception");
            }
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldMessage.getText();
        fieldMessage.setText(null);

        connection.sendMsg(fieldNickName.getText() + ": " + msg);
        printMessage(fieldNickName.getText() + ": " + msg);

    }




    @Override
    public void onConnection(Network network) {
    printMessage("Connection ready..." + "\n");
    }

    @Override
    public void onDisconnect(Network network) {
    printMessage("Connection close");
    }

    @Override
    public void onReiciveString(Network network, String msg) {

    printMessage(msg);
    }

    @Override
    public void onException(Network network, Exception e) {
    printMessage("TCPConnectionException");
    }
    public synchronized void printMessage(String value){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                log.append(value + "\n");
                log.setCaretPosition(log.getDocument().getLength());

            }
        });

    }
}
