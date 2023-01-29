package pgv.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Servidor {
	
	public static Character PREFIX = ':';

    private Vector<ConexionCliente> clientes;
    private ServerSocket socketServidor;
    private int puerto;
    
    
    protected Vector<String> nicknames;

    public Servidor(int puerto) {
        this.puerto = puerto;
        clientes = new Vector<>();
        
        nicknames = new Vector<>();
    }

    public void conectar() throws IOException {
        socketServidor = new ServerSocket(puerto);
    }

    public void desconectar() throws IOException {
        socketServidor.close();
    }

    public void enviarATodos(String mensaje) {

        for (ConexionCliente cliente : clientes) {
            cliente.enviar(mensaje);
        }
    }

    public void aceptarConexion() throws IOException {

        Socket socketCliente = socketServidor.accept();
        ConexionCliente conexionCliente =
                new ConexionCliente(this, socketCliente);
        clientes.add(conexionCliente);
        conexionCliente.start();
    	System.out.println("Nueva conexion.");
    }
    
    public void cortarCliente(ConexionCliente conexion) {
    	clientes.remove(conexion);
    	System.out.println("Conexion cerrada");
    }

    public boolean estaConectado() {
        return !socketServidor.isClosed();
    }
    
    public Vector<ConexionCliente> getOnlineClients(){
    	return this.clientes;
    }
}
