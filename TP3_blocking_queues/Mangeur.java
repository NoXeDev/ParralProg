import java.util.Random;

public  class Mangeur  implements Runnable {
    Boulangerie boulangerie;
    Random rand;

    public Mangeur(Boulangerie boulangerie, Random rand) {
        this.boulangerie = boulangerie;
        this.rand = rand;
    }


    public  void run() {

        try {

            while (true) {
                Thread.sleep(rand.nextInt(1000)) ;
                Pain pain = boulangerie.achete() ;
                if (pain != null) {
                    System.out.println("[" + Thread.currentThread().getName() +  "]" +
                            "[" + boulangerie.getStock() +  "] miam miam") ;
                }  else {
                    System.out.println("[" + Thread.currentThread().getName() +  "]" +
                            "[" + boulangerie.getStock() +  "] j'ai faim") ;
                }
            }

        }  catch (InterruptedException e) {
            System.out.println("[" + Thread.currentThread().getName() +  "] je m'arrête") ;
        }
    }
}