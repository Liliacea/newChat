import javax.sound.sampled.Port;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Network {
    private final Socket socket;
    private final Thread rxThread;
    private final  TCPConnection tcpConnection;
    private final BufferedReader in;
    private final BufferedWriter out;

    public Network(TCPConnection tcpConnection, String IP, int port) throws IOException {
    this(tcpConnection, new Socket(IP, port));
    }



    public Network(TCPConnection tcpConnection, Socket socket) throws IOException {
        this.tcpConnection = tcpConnection;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF8")));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    tcpConnection.onConnection(Network.this);

                    while (!rxThread.isInterrupted()){

                        tcpConnection.onReiciveString(Network.this, in.readLine());
                    }
                } catch (IOException e) {
                    tcpConnection.onException(Network.this, e);
                } finally {
                    tcpConnection.onDisconnect(Network.this);
                }
            }
        });
        rxThread.start();
    }
        public synchronized void sendMsg(String msg) {
            try {
                out.write(msg + "\r\n");
                out.flush();


            } catch (IOException e) {
                tcpConnection.onException(Network.this, e);
                disconnect();
            }

        }
        public synchronized void  disconnect(){
            rxThread.interrupt();
            try {
                socket.close();
            } catch (IOException e) {
                tcpConnection.onException(Network.this, e);
            }

        }

    @Override
    public String toString() {
        return "Network " + socket.getInetAddress() + socket.getPort();
    }
}

