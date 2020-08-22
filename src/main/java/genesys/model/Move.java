package genesys.model;

public class Move {
    private DiscColour discColour;
    private int column;

    public Move(DiscColour discColour, int column) {
        this.discColour = discColour;
        this.column = column;
    }

    public DiscColour getDiscColour() {
        return discColour;
    }

    public void setDiscColour(DiscColour discColour) {
        this.discColour = discColour;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
