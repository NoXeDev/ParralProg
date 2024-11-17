import java.util.Random;

public class Main {
    public static void main(String[] args) {

        final Boulangerie boulangerie = new Boulangerie();
        final Random rand = new Random();

        Boulanger boulanger = new Boulanger(boulangerie);

        Mangeur mangeur = new Mangeur(boulangerie, rand);

        Thread[] boulangers = new Thread[5];
        Thread[] mangeurs = new Thread[2];

        for (int i = 0; i < boulangers.length; i++) {
            boulangers[i] = new Thread(boulanger);
        }

        for (int i = 0; i < mangeurs.length; i++) {
            mangeurs[i] = new Thread(mangeur);
        }

        for (int i = 0; i < boulangers.length; i++) {
            boulangers[i].start();
        }

        for (int i = 0; i < mangeurs.length; i++) {
            mangeurs[i].start();
        }
    }
}