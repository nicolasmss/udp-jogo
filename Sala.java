import java.util.ArrayList;
import java.util.List;

public class Sala {
    int chaves;
    String portas;
    int mapas;
    boolean isvalid;
    List jogadores= new ArrayList();

    public Sala(int chaves,String portas,int mapas){
        this.chaves=chaves;
        this.portas=portas;
        this.mapas=mapas;
        isvalid = true;
    }

    public Sala(){
        isvalid = false;
    }

    public void addJogadores(String nome){
        jogadores.add(nome);
    }
    public void removeJogadores(String nome){
        jogadores.remove(nome);
    }

}
