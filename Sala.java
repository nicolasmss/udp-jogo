import java.util.ArrayList;
import java.util.List;

public class Sala {
    int chaves;
    String portas;
    int mapas;
    boolean isvalid;
    List<String> jogadores= new ArrayList();

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

    @Override
    public String toString() {
        String lol = "[";
        if(portas.contains("n")){
            lol= lol + " norte";
        }
        if(portas.contains("l")){
            lol= lol + " leste";
        }
        if(portas.contains("o")){
            lol= lol + " oeste";
        }
        if(portas.contains("s")){
            lol= lol + " sul";
        }
        lol = lol + " ]";

        //String aqui = "banana".replace("a", "");

        return "pessoas na sala: "+jogadores+"\nportas :"+lol
        +"\nchaves: "+chaves+"\nmapas: "+ mapas;
    }

}
