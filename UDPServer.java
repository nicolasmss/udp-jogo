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

   public static void main(String args[]) throws Exception {
      if (args.length < 1) {
         System.out.println("Usage: java UDPServer <port>");
         return;
      }

      int port = parseInt(args[0]);

      // cria socket do servidor com a porta 9876
      DatagramSocket serverSocket = new DatagramSocket(port);

      // posicao inicial
      int iniL = 0;
      int iniC = 0;
      // labirinto
      labirinto.salas = labirinto.labirinto1();

      while (true) {
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

         SocketAddress address = receivePacket.getSocketAddress();

         int escolha = parseInt(sentence.substring(0, 1));
         Jogador atual = new Jogador(" ", 0, 0, IPAddress,receivePort);
         if (escolha != 0) {
            for (Jogador j : listaJogadores) {
               if (j.ipAddress.equals(IPAddress)&&j.receivePort==receivePort) {
                  atual = j;
               }
            }
         }

         System.out.println(receivePacket.getSocketAddress());
         System.out.println(IPAddress);
         System.out.println(sentence);
         System.out.println(listaJogadores);
         System.out.println(receivePort);

         //int direcao;
         //int pegaroq;

         switch (escolha) {
            case 0:
               String nome = sentence.substring(1, 9).replace("§", "");
               listaJogadores.add(new Jogador(nome, iniL, iniC, IPAddress,receivePort));
               labirinto.salas[iniC][iniL].addJogadores(nome);
               atual = listaJogadores.get(listaJogadores.size() - 1);
               break;
            case 1:
               // 1-norte 2-leste 3-oeste 4-sul
               int direcao = parseInt(sentence.substring(1, 2));
               switch (direcao) {
                  case 1:
                     if (atual.linha == 0) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (!labirinto.salas[atual.linha - 1][atual.coluna].isvalid) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (labirinto.salas[atual.linha][atual.coluna].portas.contains("n")) {
                        System.out.println("porta fechada ai meu patrão");
                     } else {
                        labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                        atual.linha -= 1;
                        labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                     }
                     break;
                  case 2:
                     if (atual.coluna == labirinto.salas.length - 1) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (!labirinto.salas[atual.linha][atual.coluna + 1].isvalid) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (labirinto.salas[atual.linha][atual.coluna].portas.contains("l")) {
                        System.out.println("porta fechada ai meu patrão");
                     } else {
                        labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                        atual.coluna += 1;
                        labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                     }
                     break;
                  case 3:
                     if (atual.coluna == 0) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (!labirinto.salas[atual.linha][atual.coluna - 1].isvalid) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (labirinto.salas[atual.linha][atual.coluna].portas.contains("o")) {
                        System.out.println("porta fechada ai meu patrão");
                     } else {
                        labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                        atual.coluna -= 1;
                        labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                     }
                     break;
                  case 4:
                     if (atual.linha == labirinto.salas.length - 1) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (!labirinto.salas[atual.linha + 1][atual.coluna].isvalid) {
                        System.out.println("movimento invalido (tem que mandar para o client!!!)");
                     } else if (labirinto.salas[atual.linha][atual.coluna].portas.contains("s")) {
                        System.out.println("porta fechada ai meu patrão");
                     } else {
                        labirinto.salas[atual.linha][atual.coluna].removeJogadores(atual.nome);
                        atual.linha += 1;
                        labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                     }
                     break;
               }
               break;
            case 2:
               int pegaroq = parseInt(sentence.substring(1, 2));
               if(pegar(atual, pegaroq)){
                  serverSocket.send(new DatagramPacket("voce pegou o item".getBytes(), 17, IPAddress, receivePort));
               }else{
                  serverSocket.send(new DatagramPacket("falha ao pegar o item".getBytes(), 21, IPAddress, receivePort));
               }
               pegar(atual, pegaroq);
               break;
               
               
         }

         System.out.println(atual);
         System.out.println(atual.getReceivePort());

         // envia pro client
         String resposta;
         switch (escolha) {
            case 0, 1:
               resposta = labirinto.show(atual.linha, atual.coluna) + labirinto.salas[atual.linha][atual.coluna];
               serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), IPAddress, receivePort));
               break;
            case 3:
               resposta = sentence.substring(1, 51);
               resposta =  atual.nome + " falou: " + resposta;
               for (Jogador j : listaJogadores) {
                  if(!j.equals(atual)){
                     if(j.linha==atual.linha&&j.coluna==atual.coluna){
                        serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), j.getIpAddress(), j.getReceivePort()));
                     }
                  }
                  
               }
               break;
            case 4:
               resposta = sentence.substring(9, 59);
               resposta = atual.nome + " cochichou: " + resposta;
               Jogador alvo = cochicho(atual, sentence);
               System.out.println(alvo.getReceivePort());
               serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), alvo.getIpAddress(), alvo.getReceivePort()));//tem que ver isso aqui
               break;
         }

         // serverSocket.send(new DatagramPacket("mensagem recebida".getBytes(), 17,
         // IPAddress, receivePort));
      }

   }

   public static boolean pegar(Jogador atual, int objeto) {// 1=chave 2=mapa
      Sala sala = labirinto.salas[atual.linha][atual.coluna];
      if (objeto == 1) {
         if (sala.chaves >= 1) {
            sala.chaves--;
            atual.chaves++;
            return true;
         }
      } else {
         if (sala.mapas >= 1) {
            sala.mapas--;
            atual.mapas++;
            return true;
         }
      }
      return false;
   }

   public static Jogador cochicho(Jogador atual,String sentence){
      String nickalvo = sentence.substring(1,9);
      nickalvo = nickalvo.replace("§", "");
      Sala sala = labirinto.salas[atual.linha][atual.coluna];

      for (String string : sala.jogadores) {
         if(string.substring(0,nickalvo.length()).equals(nickalvo)){
            for (Jogador jogador : listaJogadores) {
               if(jogador.nome.substring(0,nickalvo.length()).equals(nickalvo)){
                  return jogador;
               }
            }
         }
      }
      return null;
   }

}
