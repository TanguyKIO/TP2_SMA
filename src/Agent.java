import java.util.List;
import java.util.Random;

public class Agent {

    private final int i;
    private final double kP, kM;
    private double fA, fB, fC;
    private final Objet[] listPrevObj;
    private Objet carriedObj;
    private final Environnement e;
    private Direction prevDir = Direction.NONE;
    private double prevSignal;
    private boolean isHelped;
    private boolean isCycling;
    private boolean canAsk;
    private double currentSignal;
    private boolean canPose;
    private Action[] prevActions;

    public Agent(int pas, int t_memoire, Environnement env, double kPlus, double kMin) {
        i = pas;
        listPrevObj = new Objet[t_memoire];
        prevActions = new Action[15];
        e = env;
        kP = kPlus;
        kM = kMin;
        currentSignal = 0;
    }

    private Objet perception() {
        Objet objUnder = e.getObjet(this);
        calcul(objUnder);
        prevSignal = currentSignal;
        currentSignal = e.getSignal(this);
        isHelped = e.isHelped(this);
        if (e.isOccupied(this)) canPose = false;
        else canPose = true;
        return objUnder;
    }

    private void calcul(Objet o) {
        if (listPrevObj.length - 1 >= 0) System.arraycopy(listPrevObj, 1, listPrevObj, 0, listPrevObj.length - 1);
        listPrevObj[listPrevObj.length - 1] = o;
        float countA = 0, countB = 0, countC = 0;
        int countHelp = 0, index = -1;
        for (Objet objet : listPrevObj) {
            if (objet != null) {
                if (objet == Objet.A) countA++;
                if (objet == Objet.B) countB++;
                if (objet == Objet.C) countC++;
            }
        }
        for (int i = 0; i < prevActions.length; i++) {
            if (prevActions[i] == Action.ASK_HELP) {
                countHelp++;
                index = i;
            }
        }
        isCycling = countHelp >= 3;
        canAsk = index > 10 || index == -1;

        fA = countA / listPrevObj.length;
        fB = countB / listPrevObj.length;
        fC = countC / listPrevObj.length;
    }

    public void finalDepot() {
        while (true) {
            perception();
            if (carriedObj == null) {
                break;
            } else if (canPose) {
                e.depot(this, carriedObj);
                carriedObj = null;
            } else {
                List<Direction> directions = e.getDirections(this);
                Direction direction = directions.get(new Random().nextInt(directions.size()));
                e.move(direction, this);
            }
        }
    }

    public void action() {
        double probPrise, probDepot;
        Objet obj = perception();
        Action action = Action.MOVE;
        List<Direction> directions = e.getDirections(this);
        Direction direction = directions.get(new Random().nextInt(directions.size()));
        double rand = new Random().nextDouble();
        if (carriedObj == null) {
            probPrise = 0;
            if (obj == Objet.C) {
                probPrise = Math.pow(kP / (kP + fC), 2);
                if (isHelped && probPrise > rand) {
                    carriedObj = obj;
                    e.priseObj(this);
                    action = Action.TAKE;
                } else if (probPrise > rand) {
                    if (canAsk && !isCycling) {
                        e.spreadSignal(this);
                        action = Action.ASK_HELP;
                    } else if (!isCycling) {
                        direction = Direction.NONE;
                        action = Action.WAIT_HELP;
                    }
                }
            } else {
                if (obj == Objet.A) {
                    probPrise = Math.pow(kP / (kP + fA), 2);
                } else if (obj == Objet.B) {
                    probPrise = Math.pow(kP / (kP + fB), 2);
                }
                if (!canPose && probPrise > rand) {
                    carriedObj = obj;
                    e.priseObj(this);
                    action = Action.TAKE;
                } else {
                    if(currentSignal>prevSignal && currentSignal >0.5) {
                        direction = prevDir;
                    } else if (currentSignal >0.5) {
                        direction = prevDir.getOpposite();
                    }
                }
            }
        } else {
            if (carriedObj == Objet.A) {
                probDepot = Math.pow(fA / (kM + fA), 2);
            } else if (carriedObj == Objet.B) {
                probDepot = Math.pow(fB / (kM + fB), 2);
            } else {
                probDepot = Math.pow(fC / (kM + fC), 2);
            }
            if (probDepot > rand && canPose) {
                e.depot(this, carriedObj);
                carriedObj = null;
                action = Action.POSE;
            }
        }
        e.move(direction, this);
        prevDir = direction;
        if (prevActions.length - 1 >= 0) System.arraycopy(prevActions, 1, prevActions, 0, prevActions.length - 1);
        prevActions[prevActions.length - 1] = action;
    }
}

enum Action {
    TAKE,
    POSE,
    ASK_HELP,
    WAIT_HELP,
    MOVE
}
