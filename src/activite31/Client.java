package activite31;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client Java. Usage : java activite31.Client [adresseIPServeur] [port]
 * Par défaut : localhost:1234. En réseau local, passer l'IP de la machine serveur.
 */
// Auteur : Hamdi Ben Amara - LSI 3 - TP3 DAR 2026
public class Client {

    public static void main(String[] args) {
        String hote = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : Server.PORT;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(InetAddress.getByName(hote), port), 5000);
            System.out.println("Connecté au serveur " + socket.getRemoteSocketAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner clavier = new Scanner(System.in);

            System.out.println("Serveur : " + in.readLine());

            while (true) {
                System.out.print("Message (quit pour sortir) > ");
                if (!clavier.hasNextLine()) break;
                String msg = clavier.nextLine();
                out.println(msg);
                String reponse = in.readLine();
                if (reponse == null) break;
                System.out.println("Serveur : " + reponse);
                if (msg.equalsIgnoreCase("quit") || msg.equalsIgnoreCase("exit")) break;
            }
        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
