import javax.swing.JTextArea;

public class LetterBox {
    String currentLetter = null;
    JTextArea leftLogArea;
    JTextArea rightLogArea;

    public LetterBox(JTextArea leftLogArea, JTextArea rightLogArea) {
        this.leftLogArea = leftLogArea;
        this.rightLogArea = rightLogArea;
    }

    public synchronized void send(String letter) {
        try {
            while(currentLetter != null) {
                wait();
            }

            System.out.println("LetterBox send: " + letter);
            leftLogArea.append("LetterBox send: " + letter + "\n");
            currentLetter = letter;
            notify();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized String receive() {
        try {
            while (currentLetter == null) {
                wait();
            }
            String letter = currentLetter; // copy

            currentLetter = null;
            notify();

            System.out.println("LetterBox receive: " + letter);
            rightLogArea.append("LetterBox receive: " + letter + "\n");
            return letter;
        }
        catch(InterruptedException e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
