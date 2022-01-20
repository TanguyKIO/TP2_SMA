import javax.swing.*;
import java.awt.*;

public class Main {

    static public void main(String args[]) {
        int mapHeight = 50;
        int mapLength = 50;
        int nbObjA = 200;
        int nbObjB = 200;
        int nbObjC = 50;
        int nbAgents = 50;
        int nbIter = 100000;
        int distSignal = 2;
        float r = (float) 0.9;
        Environnement env = new Environnement(mapHeight, mapLength, nbObjA, nbObjB, nbObjC, nbAgents, nbIter, distSignal, r);
        env.start();

        JFrame t = new JFrame("TP2");
        JPanel pan = new JPanel(new GridLayout(mapHeight, mapLength));
        JPanel[][] grille = new JPanel[mapHeight][mapLength];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapLength; j++) {
                JPanel rect = new JPanel();
                grille[i][j] = rect;
                pan.add(rect);
            }
        }
        t.add(pan);
        t.pack();
        t.setVisible(true);

        Case[][] map;
        while (true) {
            if (env.isRunning()) {
                map = env.getMap();
                for (int i = 0; i < mapHeight; i++) {
                    for (int j = 0; j < mapLength; j++) {
                        switch (map[i][j].getObj()) {
                            case A -> grille[i][j].setBackground(Color.BLUE);
                            case B -> grille[i][j].setBackground(Color.RED);
                            case C -> grille[i][j].setBackground(Color.GREEN);
                            case O -> grille[i][j].setBackground(Color.WHITE);
                        }
                    }
                }
            } else {
                break;
            }
        }
    }

}
