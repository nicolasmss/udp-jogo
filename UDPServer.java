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
         Jogador atual = new Jogador(" ", 0, 0, IPAddress, receivePort);
         if (escolha != 0) {
            for (Jogador j : listaJogadores) {
               if (j.ipAddress.equals(IPAddress) && j.receivePort == receivePort) {
                  atual = j;
               }
            }
         }

         System.out.println(receivePacket.getSocketAddress());
         System.out.println(IPAddress);
         System.out.println(sentence);
         System.out.println(listaJogadores);
         System.out.println(receivePort);
         System.out.println(sentence);

         // int direcao;
         // int pegaroq;

         switch (escolha) {
            case 0:
               String nome = sentence.substring(1, 9).replace("§", "");
               listaJogadores.add(new Jogador(nome, iniL, iniC, IPAddress, receivePort));
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
                        if (labirinto.salas[atual.linha][atual.coluna].jogadores.size() >= 1) {
                           paraTodosSala(
                                 atual.nome + " saiu da sala\n" + labirinto.salas[atual.linha][atual.coluna].jogadores,
                                 atual, serverSocket);
                        }
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
                        if (labirinto.salas[atual.linha][atual.coluna].jogadores.size() >= 1) {
                           paraTodosSala(
                                 atual.nome + " saiu da sala\n" + labirinto.salas[atual.linha][atual.coluna].jogadores,
                                 atual, serverSocket);
                        }
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
                        if (labirinto.salas[atual.linha][atual.coluna].jogadores.size() >= 1) {
                           paraTodosSala(
                                 atual.nome + " saiu da sala\n" + labirinto.salas[atual.linha][atual.coluna].jogadores,
                                 atual, serverSocket);
                        }
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
                        if (labirinto.salas[atual.linha][atual.coluna].jogadores.size() >= 1) {
                           paraTodosSala(
                                 atual.nome + " saiu da sala\n" + labirinto.salas[atual.linha][atual.coluna].jogadores,
                                 atual, serverSocket);
                        }
                        atual.linha += 1;
                        labirinto.salas[atual.linha][atual.coluna].addJogadores(atual.nome);
                     }
                     break;
               }
               break;
            case 2:
               int pegaroq = parseInt(sentence.substring(1, 2));
               if (pegar(atual, pegaroq)) {
                  serverSocket.send(new DatagramPacket("voce pegou o item".getBytes(), 17, IPAddress, receivePort));
                  if (labirinto.salas[atual.linha][atual.coluna].jogadores.size() >= 1) {
                     if (pegaroq == 1) {
                        paraTodosSala(atual.nome + " pegou uma chave\n" + "chaves: "
                              + labirinto.salas[atual.linha][atual.coluna].chaves, atual, serverSocket);
                     } else if (pegaroq == 2) {
                        paraTodosSala(atual.nome + " pegou um mapa\n" + "mapas: "
                              + labirinto.salas[atual.linha][atual.coluna].mapas, atual, serverSocket);
                     }
                  }
               } else {
                  serverSocket.send(new DatagramPacket("falha ao pegar o item".getBytes(), 21, IPAddress, receivePort));
               }
               pegar(atual, pegaroq);
               break;
            
         }

         System.out.println(atual);
         System.out.println(atual.getReceivePort());

         // envia pro client
         String resposta;
         Jogador alvo;
         switch (escolha) {
            case 0:
			case 1:
               resposta = labirinto.show(atual.linha, atual.coluna) + labirinto.salas[atual.linha][atual.coluna];
               serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), IPAddress, receivePort));
               if (labirinto.salas[atual.linha][atual.coluna].jogadores.size() > 1) {
                  paraTodosSala(atual.nome + " chegou na sala\n" + labirinto.salas[atual.linha][atual.coluna].jogadores,
                        atual, serverSocket);
               }
               break;
            case 3:
               resposta = sentence.substring(1, 51);
               resposta = atual.nome + " falou: " + resposta;
               for (Jogador j : listaJogadores) {
                  if (!j.equals(atual)) {
                     if (j.linha == atual.linha && j.coluna == atual.coluna) {
                        serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), j.getIpAddress(),
                              j.getReceivePort()));
                     }
                  }
               }
               break;
            case 4:
               resposta = sentence.substring(9, 59);
               resposta = atual.nome + " cochichou: " + resposta;
               alvo = cochicho(atual, sentence);
               System.out.println(alvo.getReceivePort());
               serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), alvo.getIpAddress(),
                     alvo.getReceivePort()));// tem que ver isso aqui
               break;
            case 5:
               System.out.println("usar item entrei");
               usarItem(sentence, atual, parseInt(sentence.substring(1, 2)), serverSocket);
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

   public static Jogador cochicho(Jogador atual, String sentence) {
      String nickalvo = sentence.substring(1, 9);
      nickalvo = nickalvo.replace("§", "");
      Sala sala = labirinto.salas[atual.linha][atual.coluna];

      for (String string : sala.jogadores) {
         if (string.substring(0, nickalvo.length()).equals(nickalvo)) {
            for (Jogador jogador : listaJogadores) {
               if (jogador.nome.substring(0, nickalvo.length()).equals(nickalvo)) {
                  return jogador;
               }
            }
         }
      }
      return null;
   }

   public static void paraTodosSala(String resposta, Jogador atual, DatagramSocket serverSocket) throws IOException {
      for (Jogador j : listaJogadores) {
         if (!j.equals(atual)) {
            if (j.linha == atual.linha && j.coluna == atual.coluna) {
               serverSocket.send(
                     new DatagramPacket(resposta.getBytes(), resposta.length(), j.getIpAddress(), j.getReceivePort()));
            }
         }
      }
   }

   public static void paraTodosSalaAtual(String resposta, Jogador atual, DatagramSocket serverSocket)
         throws IOException {
      for (Jogador j : listaJogadores) {
         if (j.linha == atual.linha && j.coluna == atual.coluna) {
            serverSocket.send(
                  new DatagramPacket(resposta.getBytes(), resposta.length(), j.getIpAddress(), j.getReceivePort()));
         }
      }
   }

   public static void usarItem(String sentence, Jogador atual, int item, DatagramSocket serverSocket)
         throws IOException {
      Sala sala = labirinto.salas[atual.linha][atual.coluna];
      String resposta="tem nada";
      switch (item) {
         case 1:// chave
            int porta = parseInt(sentence.substring(2, 3));
            switch (porta) {
               case 1:// norte
                  if (sala.portas.contains("n")) {
                     if (atual.chaves > 0) {
                        atual.chaves--;
                        sala.portas = sala.portas.replace("n", "");
                        labirinto.salas[atual.linha-1][atual.coluna].portas=labirinto.salas[atual.linha - 1][atual.coluna].portas.replace("s", "");
                        resposta= atual.nome +" abriu porta do norte";
                        paraTodosSalaAtual(sentence, atual, serverSocket);
                     }else{
                        resposta = "voce não possui chaves";
                        serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
                     }
                  }else{
                     resposta = "não há portas no norte";
                        serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
                  }
                  break;
               case 2:// leste
               if (sala.portas.contains("l")) {
                  if (atual.chaves > 0) {
                     atual.chaves--;
                     sala.portas = sala.portas.replace("l", "");
                     labirinto.salas[atual.linha][atual.coluna+1].portas=labirinto.salas[atual.linha][atual.coluna+1].portas.replace("o", "");
                     resposta= atual.nome +" abriu porta do leste";
                     paraTodosSalaAtual(resposta, atual, serverSocket);
                  }else{
                     resposta = "voce não possui chaves";
                     serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
                  }
               }else{
                  resposta = "não há portas no leste";
                     serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
               }
               break;
               case 3:// oeste
               if (sala.portas.contains("o")) {
                  if (atual.chaves > 0) {
                     atual.chaves--;
                     sala.portas = sala.portas.replace("o", "");
                     labirinto.salas[atual.linha ][atual.coluna-1].portas = labirinto.salas[atual.linha][atual.coluna-1].portas.replace("l", "");
                     resposta= atual.nome +" abriu porta do oeste";
                     paraTodosSalaAtual(resposta, atual, serverSocket);
                  }else{
                     resposta = "voce não possui chaves";
                     serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
                  }
               }else{
                  resposta = "não há portas no oeste";
                     serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
               }
               break;
               case 4:// sul
               if (sala.portas.contains("s")) {
                  if (atual.chaves > 0) {
                     atual.chaves--;
                     sala.portas = sala.portas.replace("s", " ");
                     labirinto.salas[atual.linha + 1][atual.coluna].portas = labirinto.salas[atual.linha + 1][atual.coluna].portas.replace("n", "");
                     resposta= atual.nome +" abriu porta do sul";
                     System.out.println(sala.portas);
                     paraTodosSalaAtual(resposta, atual, serverSocket);
                  }else{
                     resposta = "voce não possui chaves";
                     serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
                  }
               }else{
                  resposta = "não há portas no sul";
                     serverSocket.send(new DatagramPacket(resposta.getBytes(), resposta.length(), atual.getIpAddress(), atual.getReceivePort()));
               }
               break;
            }
            System.out.println(resposta);
            break;
         case 2:// mapa
         String mapaString;
            if(atual.mapas>0){
               mapaString = labirinto.mapa(labirinto.salas, atual.linha, atual.coluna);
               serverSocket.send(new DatagramPacket(mapaString.getBytes(), mapaString.length(), atual.getIpAddress(), atual.getReceivePort()));
            }else{
               mapaString = "não possui mapas";
               serverSocket.send(new DatagramPacket(mapaString.getBytes(), mapaString.length(), atual.getIpAddress(), atual.getReceivePort()));
            }

            break;
      }
   }

}
