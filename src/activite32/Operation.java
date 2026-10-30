package activite32;

import java.io.Serializable;

/**
 * Objet échangé entre client et serveur (communication par objets).
 * Le client remplit op1, op2 et operateur ; le serveur renvoie le même objet avec result/erreur.
 */
// Auteur : Hamdi Ben Amara - LSI 3 - TP3 DAR 2026
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private double op1;
    private double op2;
    private char operateur;
    private double result;
    private String erreur; // null si le calcul a réussi

    public Operation(double op1, double op2, char operateur) {
        this.op1 = op1;
        this.op2 = op2;
        this.operateur = operateur;
    }

    public double getOp1() { return op1; }
    public double getOp2() { return op2; }
    public char getOperateur() { return operateur; }
    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }
    public String getErreur() { return erreur; }
    public void setErreur(String erreur) { this.erreur = erreur; }

    @Override
    public String toString() {
        return op1 + " " + operateur + " " + op2;
    }
}
