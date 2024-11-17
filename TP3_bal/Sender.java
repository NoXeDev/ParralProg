import java.awt.event.KeyListener;

public class Sender extends Thread implements KeyListener {
    LetterBox letterBox;

    public Sender(LetterBox letterBox) {
        this.letterBox = letterBox;
    }

    public void run() {
        while(true);
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {}

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        String letter = String.valueOf(e.getKeyChar());
        letterBox.send(letter);

        if(letter.equals("q")) {
            System.out.println("Sender: Send quit signal");
            stop();
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {}
}
