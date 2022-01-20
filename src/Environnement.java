import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Environnement extends Thread {

    private Case[][] map;
    private ArrayList<Agent> agents = new ArrayList<>();
    private HashMap<Agent, Case> positionMap = new HashMap<>();
    private boolean running;
    private int nbIter, ds;

    public Environnement(int n, int m, int nA, int nB, int nC, int nAgents, int nbIter, int ds, float r) {
        this.nbIter = nbIter;
        map = new Case[n][m];
        int x, y;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = new Case(i, j, r);
            }
        }

        for (int i = 0; i < nA; i++) {
            while (true) {
                x = new Random().nextInt(map.length);
                y = new Random().nextInt(map[0].length);
                if (!map[x][y].isOccupied()) {
                    map[x][y].setObj(Objet.A);
                    break;
                }
            }
        }

        for (int i = 0; i < nB; i++) {
            while (true) {
                x = new Random().nextInt(map.length);
                y = new Random().nextInt(map[0].length);
                if (!map[x][y].isOccupied()) {
                    map[x][y].setObj(Objet.B);
                    break;
                }
            }
        }

        for (int i = 0; i < nC; i++) {
            while (true) {
                x = new Random().nextInt(map.length);
                y = new Random().nextInt(map[0].length);
                if (!map[x][y].isOccupied()) {
                    map[x][y].setObj(Objet.C);
                    break;
                }
            }
        }

        for (int i = 0; i < nAgents; i++) {
            Agent agent = new Agent(1, 20, this, 0.1, 0.3);
            agents.add(agent);
            x = new Random().nextInt(map.length);
            y = new Random().nextInt(map[0].length);
            positionMap.put(agent, map[x][y]);
        }
        running = true;
    }

    public ArrayList<Direction> getDirections(Agent a) {
        Case curentCase = positionMap.get(a);
        int x = curentCase.getX();
        int y = curentCase.getY();
        ArrayList<Direction> availableDirections = new ArrayList<>();
        if (x > 0) {
            availableDirections.add(Direction.TOP);
            if (y > 0) availableDirections.add(Direction.TOP_LEFT);
            if (y < map[0].length - 1) availableDirections.add((Direction.TOP_RIGHT));
        }
        if (x < map.length - 1) {
            availableDirections.add(Direction.BOTTOM);
            if (y > 0) availableDirections.add(Direction.BOTTOM_LEFT);
            if (y < map[0].length - 1) availableDirections.add((Direction.BOTTOM_RIGHT));
        }
        if (y > 0) availableDirections.add(Direction.LEFT);
        if (y < map[0].length - 1) availableDirections.add((Direction.RIGHT));
        return availableDirections;
    }

    public void move(Direction d, Agent a) {
        Case c = positionMap.get(a);
        switch (d) {
            case TOP -> positionMap.replace(a, map[c.getX() - 1][c.getY()]);
            case BOTTOM -> positionMap.replace(a, map[c.getX() + 1][c.getY()]);
            case TOP_LEFT -> positionMap.replace(a, map[c.getX() - 1][c.getY() - 1]);
            case TOP_RIGHT -> positionMap.replace(a, map[c.getX() - 1][c.getY() + 1]);
            case BOTTOM_LEFT -> positionMap.replace(a, map[c.getX() + 1][c.getY() - 1]);
            case BOTTOM_RIGHT -> positionMap.replace(a, map[c.getX() + 1][c.getY() + 1]);
            case LEFT -> positionMap.replace(a, map[c.getX()][c.getY() - 1]);
            case RIGHT -> positionMap.replace(a, map[c.getX()][c.getY() + 1]);
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

    public boolean isHelped(Agent a) {
        for(Agent agent : agents) {
            if (positionMap.get(a) == positionMap.get(agent)) return true;
        }
        return false;
    }

    public void run() {
        int pas = 20;
        for (int i = 0; i < nbIter; i++) {
            for (Agent a : agents) {
                a.action();
            }
            updateSignals();
            if (i % pas == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Agent a : agents) {
            a.finalDepot();
        }
        running = false;
    }

    private void updateSignals() {
        for (Case[] cases : map) {
            for (Case c : cases) {
                c.updateSignal();
            }
        }
    }

    public Case[][] getMap() {
        return map;
    }

    public boolean isRunning() {
        return running;
    }

    public void spreadSignal(Agent a) {
        int x = positionMap.get(a).getX();
        int y = positionMap.get(a).getY();
        int imin = x - ds;
        int jmin = y - ds;
        int imax = x + ds;
        int jmax = y + ds;
        if (imin < 0) imin = 0;
        if (jmin < 0) jmin = 0;
        if (imax > map.length - 1) imax = map.length - 1;
        if (jmax > map.length - 1) jmax = map.length - 1;
        for (int i = imin; i <= imax; i++) {
            for (int j = jmin; j <= jmax; j++) {
                int dX = Math.abs(x - i);
                int dY = Math.abs(y - j);
                if (dX > dY) map[i][j].setSignal(dX);
                else map[i][j].setSignal(dY);
            }
        }
    }

    public double getSignal(Agent agent) {
        return positionMap.get(agent).getSignal();
    }
}
