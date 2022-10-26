import java.util.ArrayList;

public class Labirinto {
    Sala [][] salas;

    public Labirinto(){
    }

    //norte [-1][  ]
    //leste [  ][+1]
    //oeste [  ][-1]
    //sul   [+1][  ]
    public Sala[][] labirinto1(){
        //[0,0]	[0,1]   [0,2]
        //[1,0]	[1,1]
        //      [2,1]

        salas = new Sala[3][3];
        boolean[] lol ={false,false,false,false}; //n,l,o,s
        salas[0][0] = new Sala(0,"",0);
        salas[0][1] = new Sala(0,"s",0);
        salas[0][2] = new Sala(1,"",0);

        salas[1][0] = new Sala(0,"l",1);
        salas[1][1] = new Sala(0,"no",0);
        salas[1][2] = new Sala();

        salas[2][0] = new Sala();
        salas[2][1] = new Sala(0,"",0);
        salas[2][2] = new Sala();

        return salas;
    }

    public String mapa(Sala[][] labirinto,int linha, int coluna){
        String lol = "";
        for (int i=0;i< labirinto.length;i++){
            for (int j=0;j< labirinto[0].length;j++){
                if (labirinto[i][j].isvalid){
                    lol=lol+"[";
                    if (linha==i&&coluna==j){
                        lol = lol +"aqui";
                    }else{
                        lol=lol+"    ";
                    }
                    lol=lol+"]";
                }
                if (j==2){
                    lol=lol+"\n";
                }else{
                    lol=lol+"\t";
                }
            }
        }
        return lol;
    }

    public String show(int linha, int coluna){
        boolean norte=true,leste=true,oeste=true,sul=true;
        if(linha==0){//norte
            norte=false;
        }else if(!salas[linha-1][coluna].isvalid){
            norte=false;
        }
        if(coluna==salas.length-1){//leste
            leste=false;
        }else if(!salas[linha][coluna+1].isvalid){
            leste =false;
        }
        if(coluna==0){//oeste
            oeste=false;
        }else if(!salas[linha][coluna-1].isvalid){
            oeste=false;
        }
        if(linha==salas.length-1){//sul
            sul=false;
        }else if(!salas[linha+1][coluna].isvalid){
            sul=false;
        }


        String lol="";
        for (int i=0;i<6;i++){
            for (int j=0;j<10;j++){
                if(i==0&&(j==0||j==9)){
                    lol=lol+" ";

                }else if(j==0||j==9){
                    if(leste&&i==3&&j==9){//leste
                        lol=lol+" ";
                    }else if(oeste&&i==3&&j==0){//oeste
                        lol=lol+" ";
                    }else{
                        lol=lol+"|";
                    }
                }else if(i==0||i==5){
                    if(norte&&j>3&&j<7&&i==0){//norte
                        lol=lol+" ";
                    }else if(sul&&j>3&&j<7&&i==5){//sul
                        lol=lol+" ";
                    }else{
                        lol=lol+"_";
                    }
                }else{
                    lol=lol+" ";
                }
                if (j==9){
                    lol=lol+"\n";
                }

            }
        }
        return lol;
    }
}