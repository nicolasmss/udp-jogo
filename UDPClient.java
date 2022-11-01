
// Le uma linha do teclado
// Envia o pacote (linha digitada) ao servidor

import java.io.*; // classes para input e output streams e
import java.net.*;// DatagramaSocket,InetAddress,DatagramaPacket
import java.util.Map;
import java.util.Scanner;

class UDPClient {
   static boolean loop = true;

   static DatagramSocket clientSocket;

   public static void main(String args[]) throws Exception {
      if (args.length < 2) {
         System.out.println("Usage: java UDPClient <server_ip> <port>");
         return;
      }
      Scanner in = new Scanner(System.in);
      Receiver receiver = new Receiver();

      String serverAddr = args[0];
      int port = Integer.parseInt(args[1]);

      // cria o stream do teclado
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

      // declara socket cliente
      clientSocket = new DatagramSocket();

      // obtem endereco IP do servidor com o DNS
      InetAddress IPAddress = InetAddress.getByName(serverAddr);
      System.out.println(IPAddress);


      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];

      // le uma linha do teclado
      String sentence;
      //sendData = sentence.getBytes();

      // cria pacote com o dado, o endereco do server e porta do servidor
      //DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
      DatagramPacket sendPacket=null;


      
      boolean nickOK = true;
      do {
         System.out.println("escreva seu nick maximo de 8 caracteres");
         sentence = "0"+inFromUser.readLine();
         for(int i=sentence.length();i<9 && sentence.length()>1;i++){
            sentence = sentence+"§";
         }
         if(sentence.length()>9){
            continue;
         }
         sendData = sentence.getBytes();
         sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
         clientSocket.send(sendPacket);

         clientSocket.receive(sendPacket);
         String nickteste = new String(sendPacket.getData());
         nickteste = nickteste.substring(0,1);
         if(nickteste.equals("1")){
            nickOK=false;
         }else{
            System.out.println("Nickname já utilizado");
         }

      }while(sentence.length()<2||sentence.length()>9||nickOK);

      receiver.start();

      boolean fim = false;
      while(loop){
         Thread.currentThread().sleep(200);
         System.out.println("Menu\n" +
                 "1 - Mover\n"+
                 "2 - Pegar\n"+
                 "3 - Falar\n"+
                 "4 - Cochichar\n"+
                 "5 - Usar item\n"+
                 "6 - Ver inventário\n"+
                 "7 - Largar item\n"+
                 "8 - Examinar item");
         int escolha = in.nextInt();

         switch (escolha){
            case 1:
               System.out.println("Mover em qual direção? (1)Norte (2)Leste (3)Oeste ou (4)Sul");
               sentence = escolha + inFromUser.readLine();
               if(sentence.length()!=2){
                  System.out.println("Escreva uma opção válida");
                  continue;
               }
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 2:
               System.out.println("Deseja pegar? (1)Chave (2)Mapa (3)Tesouro");
               sentence = escolha + inFromUser.readLine();
               if(sentence.length()!=2){
                  System.out.println("Escreva uma opção válida");
                  continue;
               }
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 3:
               System.out.println("Escreva sua mensagem:(max: 50 caracteres)");
               sentence = escolha + inFromUser.readLine();
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 4:
               System.out.println("Para quem voce deseja escrever?(max: 8 caracteres)");
               String nickname = inFromUser.readLine();
               if(nickname.length()>8||nickname.length()<1){
                  System.out.println("Nome invalido");
                  continue;
               }
               for(int i=nickname.length();i<8;i++){
                  nickname = nickname+"§";
               }
               sentence = escolha + nickname;
               System.out.println("Escreva sua mensagem:(max: 50 caracteres)");
               sentence = sentence +""+ inFromUser.readLine();
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 5:
               System.out.println("Que item voce deseja usar? (1)Chave (2)Mapa");
               int escolhaItem = in.nextInt();
               sentence = escolha + "" + escolhaItem + "";
               if(sentence.length()!=2){
                  System.out.println("Escreva uma opção válida");
                  continue;
               }
               if(escolhaItem==1){
                  System.out.println("Qual porta voce deseja abrir: (1)Norte (2)Leste (3)Oeste (4)Sul");
                  sentence = escolha +""+ escolhaItem +""+ inFromUser.readLine();
                  if(sentence.length()!=3){
                     System.out.println("Escreva uma opção válida");
                     continue;
                  }
               }
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 6:
               sentence = escolha+"";
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 7:
               System.out.println("Deseja largar? (1)Chave (2)Mapa");
               sentence = escolha + inFromUser.readLine();
               if(sentence.length()!=2){
                  System.out.println("Escreva uma opção válida");
                  continue;
               }
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
            case 8:
               System.out.println("Deseja examinar? (1)Chave (2)Mapa");
               sentence = escolha + inFromUser.readLine();
               if(sentence.length()!=2){
                  System.out.println("Escreva uma opção válida");
                  continue;
               }
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
         }

         if(fim){
            break;
         }
         //envia o pacote
         if(escolha>0&&escolha<9){
            clientSocket.send(sendPacket);
         }else{
            System.out.println("Escreva uma opção válida");
         }
      }



      // recebe msg do server
      //DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      //clientSocket.receive(receivePacket);
      //System.out.println(new String(receivePacket.getData()));

      // fecha o cliente
      clientSocket.close();
   }

   public static class Receiver extends Thread{


      public void run() {
         while(loop){
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
               clientSocket.receive(receivePacket);
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
            String str = new String (receivePacket.getData()).trim();
            if(str.contains("FIM159753")){
               System.out.println(str.substring(0, str.length()-9));
               loop=false;
            }else{
               System.out.println(new String(receivePacket.getData()));
            }
         }
      }
   }



}
