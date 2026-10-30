package activite31;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activité 3-1 : serveur multi-thread.
 * Le serveur accepte les clients en boucle et délègue chacun à un thread ClientProcess.
 */
// Auteur : Hamdi Ben Amara - LSI 3 - TP3 DAR 2026
public class Server {

    public static final int PORT = 1234;

    public static void main(String[] args) {
        int numeroClient = 0;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur multi-thread démarré sur le port " + PORT + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                numeroClient++;
                System.out.println("Client n°" + numeroClient + " connecté - adresse : "
                        + socket.getRemoteSocketAddress());
                new ClientProcess(socket, numeroClient).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }
}
