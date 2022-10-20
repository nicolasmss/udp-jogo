
// Le uma linha do teclado
// Envia o pacote (linha digitada) ao servidor

import java.io.*; // classes para input e output streams e
import java.net.*;// DatagramaSocket,InetAddress,DatagramaPacket
import java.util.Map;
import java.util.Scanner;

class UDPClient {

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
      DatagramPacket sendPacket;


      receiver.start();

      do {
         System.out.println("escreva seu nick maximo de 8 caracteres");
         sentence = "0"+inFromUser.readLine();
         sendData = sentence.getBytes();
         sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
         clientSocket.send(sendPacket);
      }while(sentence.length()<2||sentence.length()>=9);


      boolean fim = false;
      while(true){
         System.out.println("Menu\n" +
                 "1 - mover");
         int escolha = in.nextInt();

         switch (escolha){
            case 1:
               System.out.println("mover em qual direção? (1)Norte (2)Leste (3)Oeste ou (4)Sul");
               sentence = "1" + inFromUser.readLine();
               sendData = sentence.getBytes();
               sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
               break;
         }

         if(fim){
            break;
         }
         //envia o pacote
         clientSocket.send(sendPacket);
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
         byte[] receiveData = new byte[1024];
         while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
               clientSocket.receive(receivePacket);
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
            System.out.println(new String(receivePacket.getData()));
         }
      }
   }



}
