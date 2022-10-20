// Recebe um pacote de algum cliente
// Separa o dado, o endereco IP e a porta deste cliente
// Imprime o dado na tela

import java.io.*;
import java.net.*;
import java.util.*;

import static java.lang.Integer.parseInt;

class UDPServer {
   static List<Jogador> listaJogadores = new ArrayList<>();
   static Labirinto labirinto = new Labirinto();

   public static void main(String args[])  throws Exception
      {
         if (args.length < 1) {
            System.out.println("Usage: java UDPServer <port>");
            return;
         }

         int port = parseInt(args[0]);

         // cria socket do servidor com a porta 9876
         DatagramSocket serverSocket = new DatagramSocket(port);

         //posicao inicial
         int iniL=0;
         int iniC=0;
         //labirinto
         labirinto.salas = labirinto.labirinto1();


            while(true)
               {
                  byte[] receiveData = new byte[1024];
                  // declara o pacote a ser recebido
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                  // recebe o pacote do cliente
                  serverSocket.receive(receivePacket);

                  // pega os dados, o endereco IP e a porta do cliente
                  // para poder mandar a msg de volta
                  String sentence = new String(receivePacket.getData());
                  InetAddress IPAddress = receivePacket.getAddress();
                  int receivePort = receivePacket.getPort();

                  SocketAddress address =  receivePacket.getSocketAddress();

                  int escolha = parseInt(sentence.substring(0,1));
                  Jogador atual=new Jogador(" ",0,0,address);
                  if (escolha!=0){
                     for (Jogador j: listaJogadores){
                        if (j.address.equals(address)){
                           atual = j;
                        }
                     }
                  }

                  
                  System.out.println(receivePacket.getSocketAddress());
                  System.out.println(IPAddress);
                  System.out.println(sentence);
                  System.out.println(listaJogadores);

                  switch(escolha){
                     case 0:
                        listaJogadores.add(new Jogador(sentence.substring(1,9),iniL,iniC,address));
                        labirinto.salas[iniC][iniL].addJogadores(sentence.substring(1,9));
                        atual = listaJogadores.get(listaJogadores.size()-1);
                        break;
                     case 1:
                        //1-norte 2-leste 3-oeste 4-sul
                        int direcao = parseInt(sentence.substring(1,2));
                        switch (direcao){
                           case 1:
                              if(atual.linha==0){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else if(!labirinto.salas[atual.linha-1][atual.coluna].isvalid){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else{
                                 labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                                 atual.linha-=1;
                                 labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                              }
                              break;
                           case 2:
                              if(atual.coluna==labirinto.salas.length-1){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else if(!labirinto.salas[atual.linha][atual.coluna+1].isvalid){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else{
                                 labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                                 atual.coluna+=1;
                                 labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                              }
                              break;
                           case 3:
                              if(atual.coluna==0){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else if(!labirinto.salas[atual.linha][atual.coluna-1].isvalid){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else{
                                 labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                                 atual.coluna-=1;
                                 labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                              }
                              break;
                           case 4:
                              if(atual.linha==labirinto.salas.length-1){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else if(!labirinto.salas[atual.linha+1][atual.coluna].isvalid){
                                 System.out.println("movimento invalido (tem que mandar para o client!!!)");
                              }else{
                                 labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                                 atual.linha+=1;
                                 labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                              }
                              break;
                        }
                        break;
                  }

                  System.out.println(atual);


                  //envia pro client
                  switch(escolha){
                     case 0,1:
                        serverSocket.send(new DatagramPacket(labirinto.show(atual.linha, atual.coluna).getBytes(), labirinto.show(atual.linha, atual.coluna).length(), IPAddress, receivePort));
                        break;
                  }

                  //serverSocket.send(new DatagramPacket("mensagem recebida".getBytes(), 17, IPAddress, receivePort));
               }
      }
}
