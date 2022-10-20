import java.util.List;

public class Nodos {
    public Nodos norte;
    public Nodos leste;
    public Nodos oeste;
    public Nodos sul;

    public List objetos;
    public List pessoas;

    public Nodos(){

    }

    public void addObjetos(String obj){
        objetos.add(obj);
    }

    public void removeObjetos(String obj){
        objetos.remove(obj);
    }

    public void addPessoas(String pessoa){
        pessoas.add(pessoa);
    }

    public void removePessoa(String pessoa){
        pessoas.remove(pessoa);
    }
}