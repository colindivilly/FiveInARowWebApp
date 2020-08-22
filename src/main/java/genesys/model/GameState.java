package genesys.model;

public class GameState {
    private DiscColour[][] gameGrid;
    private Player winningPlayer;
    private boolean isGameOver;


    public DiscColour[][] getGameGrid() {
        return gameGrid;
    }

    public void setGameGrid(DiscColour[][] gameGrid) {
        this.gameGrid = gameGrid;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(Player winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

}
