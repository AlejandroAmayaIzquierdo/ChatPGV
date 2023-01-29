package pgv.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread {
	
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private String recivedMessage = "";
    
    public static void main(String[] args) throws IOException {
		new Cliente(args[0], Integer.parseInt(args[1])).start();
	}
    
    public Cliente(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        scanner = new Scanner(System.in);
    }
    
    @Override
    public void run() {
        // Crear hilos para recibir y enviar mensajes
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (recivedMessage != null) {
                    try {
                        recivedMessage = in.readLine();
                        if(recivedMessage == null)
                        	break;
                        System.out.println(recivedMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    socket.close();
                    in.close();
                    out.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            	System.exit(MAX_PRIORITY);
            }
        });
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(recivedMessage == null)
                    	break;
                    String message = scanner.nextLine();
                    out.println(message);
                }
            }
            
        });

        receiveThread.start();
        sendThread.start();
    }

}
