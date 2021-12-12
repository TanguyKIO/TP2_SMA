import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Environnement extends Thread {

    private Case[][] map;
    private ArrayList<Agent> agents = new ArrayList<>();
    private HashMap<Agent, Case> positionMap = new HashMap<>();
    private boolean ready;
    private int nbIter;

    public Environnement(int n, int m, int nA, int nB, int nAgents, int nbIter, boolean withError) {
        this.nbIter = nbIter;
        map = new Case[n][m];
        int x, y;

        for(int i =0; i <n ; i++){
            for(int j = 0; j < m; j++) {
                map[i][j] = new Case(i,j);
            }
        }

        for (int i = 0; i < nA; i++) {
            while (true) {
                x = new Random().nextInt(map.length);
                y = new Random().nextInt(map[0].length);
                if(!map[x][y].isOccupied()) {
                    map[x][y].setObj(Objet.A);
                    break;
                }
            }
        }

        for (int i = 0; i < nB; i++) {
            while (true) {
                x = new Random().nextInt(map.length);
                y = new Random().nextInt(map[0].length);
                if(!map[x][y].isOccupied()) {
                    map[x][y].setObj(Objet.B);
                    break;
                }
            }
        }

        for (int i = 0; i < nAgents; i++) {
            Agent agent = new Agent(1, 20, this, 0.1, 0.3, 0.1, withError);
            agents.add(agent);
            x = new Random().nextInt(map.length);
            y = new Random().nextInt(map[0].length);
            positionMap.put(agent, map[x][y]);
        }
        ready = true;
    }

    public ArrayList<Direction> getDirections(Agent a) {
        Case curentCase = positionMap.get(a);
        int x = curentCase.getX();
        int y = curentCase.getY();
        ArrayList<Direction> availableDirections = new ArrayList<>();
        if (x > 0) {
            availableDirections.add(Direction.TOP);
            if (y > 0) availableDirections.add(Direction.TOP_LEFT);
            if (y < map[0].length-1) availableDirections.add((Direction.TOP_RIGHT));
        }
        if (x < map.length-1) {
            availableDirections.add(Direction.BOTTOM);
            if (y > 0) availableDirections.add(Direction.BOTTOM_LEFT);
            if (y < map[0].length-1) availableDirections.add((Direction.BOTTOM_RIGHT));
        }
        if (y > 0) availableDirections.add(Direction.LEFT);
        if (y < map[0].length-1) availableDirections.add((Direction.RIGHT));
        return availableDirections;
    }

    public void move(Direction d, Agent a) {
        Case c = positionMap.get(a);
        switch (d) {
            case TOP -> positionMap.replace(a,map[c.getX()-1][c.getY()]);
            case BOTTOM -> positionMap.replace(a,map[c.getX()+1][c.getY()]);
            case TOP_LEFT -> positionMap.replace(a,map[c.getX()-1][c.getY()-1]);
            case TOP_RIGHT -> positionMap.replace(a,map[c.getX()-1][c.getY()+1]);
            case BOTTOM_LEFT -> positionMap.replace(a,map[c.getX()+1][c.getY()-1]);
            case BOTTOM_RIGHT -> positionMap.replace(a,map[c.getX()+1][c.getY()+1]);
            case LEFT -> positionMap.replace(a,map[c.getX()][c.getY()-1]);
            case RIGHT -> positionMap.replace(a,map[c.getX()][c.getY()+1]);
        }
    }

    public Objet getObjet(Agent a) {
        return positionMap.get(a).getObj();
    }

    public void depot(Agent agent, Objet objPorte) {
        Case c = positionMap.get(agent);
        c.setObj(objPorte);
    }

    public void priseObj(Agent agent) {
        Case c = positionMap.get(agent);
        c.removeObj();
    }

    public boolean isOccupied(Agent a) {
        return positionMap.get(a).isOccupied();
    }

    public void run(){
        int pas = 20;
        for(int i = 0; i< nbIter; i++){
            for(Agent a : agents){
                a.action();
            }
            if(i % pas == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for(Agent a : agents) {
            a.finalDepot();
        }
    }

    public Case[][] getMap() {
        return map;
    }

    public boolean isReady() {
        return ready;
    }
}

enum Direction {
    TOP,
    BOTTOM,
    RIGHT,
    LEFT,
    TOP_RIGHT,
    TOP_LEFT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT
}
