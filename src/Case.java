import java.util.Objects;

public class Case {
    private Objet obj;
    private int x, y;
    private float signal, r;

    public Case(int x, int y, float r) {
        this.obj = Objet.O;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void setObj(Objet obj) {
        this.obj = obj;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Objet getObj() {
        return obj;
    }

    public void removeObj() {
        obj = Objet.O;
    }

    public void setSignal(int dist) {
        if (dist == 0) signal = 1;
        else {
            int newSignal = 1;
            for (int i = 1; i <= dist; i++) {
                newSignal -= (newSignal / dist);
            }
            if(newSignal > signal) signal = newSignal;
        }
    }

    public float getSignal() {
        return signal;
    }

    public void updateSignal() {
        signal *= r;
    }

    public boolean isOccupied() {
        return obj != Objet.O;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return x == aCase.x && y == aCase.y;
    }
}

enum Objet {
    A,
    B,
    C,
    O
}
