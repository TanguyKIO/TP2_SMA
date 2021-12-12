import java.util.List;
import java.util.Random;

public class Agent {

    private int i;
    private final double kP;
    private final double kM;
    private double fA;
    private double fB;
    private final double error;
    private final Objet[] listPrevObj;
    private Objet objPorte;
    private Environnement e;
    private boolean peutDeposer;
    private final boolean withError;

    public Agent(int pas, int t_memoire, Environnement env, double kPlus, double kMin, double error, boolean withError) {
        this.withError = withError;
        i = pas;
        listPrevObj = new Objet[t_memoire];
        e = env;
        kP = kPlus;
        kM = kMin;
        this.error = error;
    }

    private Objet perception() {
        Objet objUnder = e.getObjet(this);
        if(withError) calculWithError(objUnder);
        else calcul(objUnder);
        if (e.isOccupied(this)) peutDeposer = false;
        else peutDeposer = true;
        return objUnder;
    }

    private void calcul(Objet o) {
        if (listPrevObj.length - 1 >= 0) System.arraycopy(listPrevObj, 1, listPrevObj, 0, listPrevObj.length - 1);
        listPrevObj[listPrevObj.length - 1] = o;
        float countA = 0, countB = 0;
        for (Objet objet : listPrevObj) {
            if (objet != null) {
                if (objet == Objet.A) countA++;
                if (objet == Objet.B) countB++;
            }
        }
        fA = countA / listPrevObj.length;
        fB = countB / listPrevObj.length;
    }

    private void calculWithError(Objet o) {
        if (listPrevObj.length - 1 >= 0) System.arraycopy(listPrevObj, 1, listPrevObj, 0, listPrevObj.length - 1);
        listPrevObj[listPrevObj.length - 1] = o;
        int countA = 0, countB = 0;
        for (Objet objet : listPrevObj) {
            if (objet == Objet.A) countA++;
            if (objet == Objet.B) countB++;
        }
        fA = (countA + countB * error) / listPrevObj.length;
        fB = (countB + countA * error) / listPrevObj.length;
    }

    public void finalDepot(){
        while(true){
            if(objPorte == null) {
                break;
            } else if( peutDeposer) {
                e.depot(this, objPorte);
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
        List<Direction> directions = e.getDirections(this);
        Direction direction = directions.get(new Random().nextInt(directions.size()));
        double rand = new Random().nextDouble();
        if (objPorte == null) {
            probPrise = 0;
            if (obj == Objet.A) {
                probPrise = Math.pow(kP / (kP + fA), 2);
            } else if (obj == Objet.B) {
                probPrise = Math.pow(kP / (kP + fB), 2);
            }
            if (!peutDeposer && probPrise > rand) {
                objPorte = obj;
                e.priseObj(this);
            }
        } else {
            if (objPorte == Objet.A) {
                probDepot = Math.pow(fA / (kM + fA), 2);
            } else {
                probDepot = Math.pow(fB / (kM + fB), 2);
            }
            if (probDepot > rand && peutDeposer) {
                e.depot(this, objPorte);
                objPorte = null;
            }
        }
        e.move(direction, this);
    }
}
