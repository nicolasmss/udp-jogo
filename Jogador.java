import java.net.InetAddress;
import java.net.SocketAddress;

public class Jogador {
    String nome;
    int linha;
    int coluna;
    int chaves;
    int mapas;
    SocketAddress address;

    public Jogador (String nome,int linha, int coluna, SocketAddress address){
        this.nome=nome;
        this.linha=linha;
        this.coluna=coluna;
        this.address = address;
    }

    public String toString(){
        return nome+linha+":"+coluna;
    }
}
