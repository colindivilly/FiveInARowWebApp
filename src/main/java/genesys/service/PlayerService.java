package genesys.service;

import genesys.model.DiscColour;
import genesys.controllers.GameRuleException;
import genesys.model.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    void createNewPlayer(Player player) throws GameRuleException;
    Optional<Player> whichPlayersTurnIsIt(DiscColour coloursTurn);
    List<Player> getCurrentPlayers();
}
