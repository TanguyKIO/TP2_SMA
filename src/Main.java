import javax.swing.*;
import java.awt.*;

public class Main{

    static public void main(String args[]) {
        int mapHeight = 50;
        int mapLength = 50;
        int nbObjA = 200;
        int nbObjB = 200;
        int nbAgents = 50;
        boolean withError = true;
        Environnement env = new Environnement(mapHeight, mapLength, nbObjA, nbObjB, nbAgents,100000, withError);
        env.start();

        JFrame t = new JFrame("TP2");
        JPanel pan = new JPanel (new GridLayout(mapHeight,mapLength));
        JPanel[][] grille = new JPanel[mapHeight][mapLength];
        for(int i = 0; i<mapHeight;i++){
            for(int j = 0;j<mapLength; j++){
                JPanel rect = new JPanel();
                grille[i][j] = rect;
                pan.add(rect);
            }
        }
        t.add(pan);
        t.pack();
        t.setVisible(true);

        Case[][] map;
        while(true) {
            if(env.isReady()){
                map = env.getMap();
                for(int i = 0; i<mapHeight;i++){
                    for(int j = 0;j<mapLength; j++){
                        switch(map[i][j].getObj()) {
                            case A -> grille[i][j].setBackground(Color.BLUE);
                            case B -> grille[i][j].setBackground(Color.RED);
                            case O -> grille[i][j].setBackground(Color.WHITE);
                        }
                    }
                }
            }
        }
    }

}
