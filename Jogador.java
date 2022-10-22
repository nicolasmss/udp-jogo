import java.net.InetAddress;
import java.net.SocketAddress;

public class Jogador {
    String nome;
    int linha;
    int coluna;
    int chaves=0;
    int mapas=0;
    InetAddress ipAddress;
    int receivePort;

    public Jogador (String nome,int linha, int coluna, InetAddress ipAddress, int receivePort){
        this.nome=nome;
        this.linha=linha;
        this.coluna=coluna;
        this.ipAddress = ipAddress;
        this.receivePort = receivePort;
    }

    public String toString(){
        return nome+linha+":"+coluna+"--"+ipAddress+receivePort;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }
    public int getReceivePort() {
        return receivePort;
    }
}
