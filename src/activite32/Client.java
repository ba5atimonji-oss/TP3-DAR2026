package activite32;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client calculatrice (communication par objets).
 * Usage : java activite32.Client [adresseIPServeur] [port]
 */
// Auteur : Hamdi Ben Amara - LSI 3 - TP3 DAR 2026
public class Client {

    public static void main(String[] args) {
        String hote = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : Server.PORT;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(InetAddress.getByName(hote), port), 5000);
            System.out.println("Connecté au serveur " + socket.getRemoteSocketAddress());

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner clavier = new Scanner(System.in);

            System.out.println("Serveur : " + in.readObject());

            while (true) {
                System.out.print("Opération (ex: 12 + 5, 'q' pour quitter) > ");
                if (!clavier.hasNextLine()) break;
                String ligne = clavier.nextLine().trim();
                if (ligne.equalsIgnoreCase("q") || ligne.equalsIgnoreCase("quit")) break;

                String[] t = ligne.split("\\s+");
                if (t.length != 3 || t[1].length() != 1) {
                    System.out.println("Format invalide. Exemple : 12 + 5");
                    continue;
                }
                try {
                    Operation op = new Operation(Double.parseDouble(t[0]), Double.parseDouble(t[2]), t[1].charAt(0));
                    out.writeObject(op);
                    out.flush();
                    out.reset();
                    Operation rep = (Operation) in.readObject();
                    if (rep.getErreur() != null) System.out.println("Erreur : " + rep.getErreur());
                    else System.out.println("Résultat : " + rep + " = " + rep.getResult());
                } catch (NumberFormatException e) {
                    System.out.println("Nombres invalides.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
