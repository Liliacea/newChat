import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnection{
    ArrayList<Network> connections = new ArrayList<>();

    public static void main(String[] args) {
        Server server = new Server();

    }

    public Server() {
        System.out.println("server running...");
        try (ServerSocket serverSocket = new ServerSocket(10001)){
            try {
                while (true){
                    new Network (this, serverSocket.accept());
                }
            }
            catch (IOException e) {
                System.out.println("TCPConnection exception " + e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public synchronized void onConnection(Network network) {
    connections.add(network);
    messagetoAll(network + "connect" );
    }

    @Override
    public synchronized void onDisconnect(Network network) {
    connections.remove(network);
    messagetoAll(network + "disconnect");
    }

    @Override
    public synchronized void onReiciveString(Network network, String msg) {
    messagetoAll(msg);
    }

    @Override
    public synchronized void onException(Network network, Exception e) {
        System.out.println("TCPException " + e);
    }

    public synchronized void messagetoAll(String msg){
        System.out.println(msg);
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).sendMsg(msg);

        }

        }
    }





