package genesys.service;

import genesys.controllers.GameRuleException;
import genesys.model.DiscColour;
import genesys.model.Move;

public interface GameGridService {
    DiscColour getGameWinner();
    boolean isGameOver();
    void addDisc(Move move) throws GameRuleException;
    DiscColour[][] getGameGrid();
    DiscColour whichColourHasTheNextTurn();
    void restartGame();
}
