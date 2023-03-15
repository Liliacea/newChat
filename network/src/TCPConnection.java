public interface TCPConnection {
    void onConnection(Network network);

    void onDisconnect(Network network);
    void onReiciveString(Network network, String msg);
    void onException(Network network, Exception e);


}
