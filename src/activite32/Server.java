package activite32;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Activité 3-2 : serveur multi-thread de calculatrice (communication par objets).
 * Contient le compteur global partagé du nombre d'opérations traitées.
 */
// Auteur : Hamdi Ben Amara - LSI 3 - TP3 DAR 2026
public class Server {

    public static final int PORT = 1234;

    // Ressource critique partagée entre tous les threads ClientProcess
    private static int compteurGlobal = 0;

    /** Incrémente le compteur de façon atomique et retourne la nouvelle valeur. */
    public static synchronized int incrementerCompteur() {
        compteurGlobal++;
        return compteurGlobal;
    }

    public static void main(String[] args) {
        int numeroClient = 0;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur calculatrice multi-thread démarré sur le port " + PORT + "...");
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
