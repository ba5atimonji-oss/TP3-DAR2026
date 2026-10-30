package activite32;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Thread dédié à un client : reçoit des objets Operation, calcule et renvoie le résultat.
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
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.flush(); // envoie l'en-tête du flux avant que le client ne construise son ObjectInputStream
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject("Bienvenue ! Vous êtes le client n°" + numero);
                out.flush();

                while (true) {
                    Operation op;
                    try {
                        op = (Operation) in.readObject();
                    } catch (EOFException e) {
                        break; // le client a fermé la connexion
                    }
                    calculer(op);
                    // Incrémentation ET affichage dans la même section critique :
                    // l'ordre des lignes affichées suit alors l'ordre du compteur.
                    synchronized (Server.class) {
                        int total = Server.incrementerCompteur();
                        System.out.println("[Client n°" + numero + "] " + op + " = "
                                + (op.getErreur() == null ? op.getResult() : op.getErreur())
                                + " | compteur global = " + total);
                    }
                    out.writeObject(op);
                    out.flush();
                    out.reset(); // évite que le flux ne garde en cache l'ancien objet
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur avec le client n°" + numero + " : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            System.out.println("Client n°" + numero + " déconnecté.");
        }
    }

    private void calculer(Operation op) {
        switch (op.getOperateur()) {
            case '+': op.setResult(op.getOp1() + op.getOp2()); break;
            case '-': op.setResult(op.getOp1() - op.getOp2()); break;
            case '*': op.setResult(op.getOp1() * op.getOp2()); break;
            case '/':
                if (op.getOp2() == 0) op.setErreur("Division par zéro");
                else op.setResult(op.getOp1() / op.getOp2());
                break;
            default:
                op.setErreur("Opérateur inconnu : " + op.getOperateur());
        }
    }
}
