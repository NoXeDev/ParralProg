import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Système de BAL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        
        JTextArea leftLogArea = new JTextArea();
        JTextArea rightLogArea = new JTextArea();

        leftLogArea.setEditable(false);
        rightLogArea.setEditable(false);
        
        JScrollPane leftScrollPane = new JScrollPane(leftLogArea);
        JScrollPane rightScrollPane = new JScrollPane(rightLogArea);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        splitPane.setDividerLocation(400);
        
        // Ajouter le panneau divisé à la fenêtre
        frame.add(splitPane, BorderLayout.CENTER);
    
        LetterBox letterBox = new LetterBox(leftLogArea, rightLogArea);
        Receiver receiver = new Receiver(letterBox);
        Sender sender = new Sender(letterBox);

        frame.addKeyListener(sender);
        frame.setFocusable(true); 
        frame.setFocusTraversalKeysEnabled(false); 
        frame.setVisible(true);

        receiver.start();
        sender.start();

        while(sender.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }
}