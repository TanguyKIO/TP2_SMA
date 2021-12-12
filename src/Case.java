public class Case {
    private Objet obj;
    private int x,y;

    public Case(int x, int y) {
        this.obj = Objet.O;
        this.x = x;
        this.y = y;
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

    public boolean isOccupied() {
        return obj !=  Objet.O;
    }
}

enum Objet {
    A,
    B,
    O
}
