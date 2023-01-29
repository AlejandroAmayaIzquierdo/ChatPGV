package pgv.main;
import java.io.IOException;

import pgv.server.Servidor;

/**
 * Created by dam on 24/01/18.
 */
public class Principal {

    public static void main(String args[]) {
        final int PUERTO = 50555;

        Servidor servidor = new Servidor(PUERTO);
        try {
            servidor.conectar();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        while (servidor.estaConectado()) {
            try {
                servidor.aceptarConexion();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
