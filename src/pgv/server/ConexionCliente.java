package pgv.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;


public class ConexionCliente extends Thread {
	
	private String nickName;
	
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private Servidor servidor;

    public ConexionCliente(Servidor servidor, Socket socket) throws IOException {
        this.servidor = servidor;
        this.socket = socket;
        salida = new PrintWriter(socket.getOutputStream(), true, Charset.forName("UTF8"));
        entrada = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
    }

    @Override
    public void run() {
    	
        salida.println("Bienvenido al Chat");
        while(nickName == null) {
            salida.println("Identicate ':nickname <nombre>'");
            String nameLine = null;
			try {
				nameLine = entrada.readLine();
				commands(nameLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }


        salida.println("Hola " + nickName + " ,ya puedes enviar mensajes de texto");

        String mensaje = null;
        try {
            while ((mensaje = entrada.readLine()) != null && !mensaje.equals(Servidor.PREFIX + "bye")) {
            	if(mensaje.charAt(0) != Servidor.PREFIX) {
            		 servidor.enviarATodos("<" + nickName + "> " + mensaje);
            		 continue;
            	}
            	
            	commands(mensaje);
               
            }
            cortarConexionCliente();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void cortarConexionCliente() throws IOException {
        servidor.cortarCliente(this);
        salida.println("** ¡¡ Hasta Pronto !! **");
        entrada.close();
        salida.close();
        socket.close();
    }
    
    public void commands(String mensaje) {
		String[] argumentos = mensaje.split(" ");
    	switch (argumentos[0]) {
		case ":nickname":
			boolean isUse = false;
			int n = 0;
			while(!isUse && n < this.servidor.getOnlineClients().size()) {
				if(this.servidor.getOnlineClients().get(n).nickName != null && 
						this.servidor.getOnlineClients().get(n).nickName.equals(argumentos[1])) {
					salida.println("Este nombre esta siendo usado");
					isUse = true;
				}
				n++;
			}
			if(isUse)
				break;
			
			this.nickName = argumentos[1];
			if(servidor.nicknames.contains(argumentos[1]))
				break;
			servidor.nicknames.add(argumentos[1]);
			break;
		default:
			break;
		}
    }

    public void enviar(String mensaje) {
        salida.println(mensaje);
    }

	@Override
	public int hashCode() {
		return Objects.hash(socket);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConexionCliente other = (ConexionCliente) obj;
		return Objects.equals(socket.getLocalPort(), other.socket.getLocalPort());
	}
    
}
