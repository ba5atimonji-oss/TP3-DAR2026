package activite31;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Thread dédié à un client : envoie son numéro d'ordre puis renvoie (écho) chaque ligne reçue.
 * Compatible avec un client Telnet (texte brut).
 */
// Auteur : Hamdi Ben Amara - LSI 3 - TP3 DAR 2026
public class ClientProcess extends Thread {

    private final Socket socket;
    private final int numero;

    public ClientProcess(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Bienvenue ! Vous êtes le client n°" + numero);

            String ligne;
            while ((ligne = in.readLine()) != null) {
                System.out.println("[Client n°" + numero + "] " + ligne);
                if (ligne.equalsIgnoreCase("quit") || ligne.equalsIgnoreCase("exit")) {
                    out.println("Au revoir client n°" + numero);
                    break;
                }
                out.println("Echo (client n°" + numero + ") : " + ligne);
            }
        } catch (IOException e) {
            System.err.println("Erreur avec le client n°" + numero + " : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            System.out.println("Client n°" + numero + " déconnecté.");
        }
    }
}
