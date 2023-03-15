public interface TCPConnection {
    public void onConnection(Network network);

    public void onDisconnect(Network network);
    public void onReiciveString(Network network, String msg);
    public void onException(Network network, Exception e);


}
