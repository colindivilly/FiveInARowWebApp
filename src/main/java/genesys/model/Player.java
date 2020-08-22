package genesys.model;

public class Player {

    private String name;
    private DiscColour discColour;

    public Player(String name, DiscColour discColour) {
        this.name = name;
        this.discColour = discColour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiscColour getDiscColour() {
        return discColour;
    }

    public void setDiscColour(DiscColour discColour) {
        this.discColour = discColour;
    }

}
