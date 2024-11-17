public class Receiver extends Thread {
    LetterBox letterBox;

    public Receiver(LetterBox letterBox) {
        this.letterBox = letterBox;
    }

    public void run() {
        while(true) {
            String letter = letterBox.receive();
            if(letter.equals("q")) {
                System.out.println("Receiver: Receive quit signal");
                break;
            }
        }
    }
}
