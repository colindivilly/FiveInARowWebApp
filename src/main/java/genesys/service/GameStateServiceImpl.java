package genesys.service;

import genesys.model.DiscColour;
import genesys.model.GameState;
import genesys.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameStateServiceImpl implements GameStateService {

    @Autowired
    private final GameGridService gameGridService;

    @Autowired
    private final PlayerService playerService;

    public GameStateServiceImpl(GameGridService gameGridService, PlayerService playerService) {
        this.gameGridService = gameGridService;
        this.playerService = playerService;
    }

    @Override
    public GameState getGameState() {
        GameState gameState = new GameState();
        gameState.setGameGrid(gameGridService.getGameGrid());
        boolean isGameOver = gameGridService.isGameOver();
        gameState.setGameOver(isGameOver);

        if(isGameOver && gameGridService.getGameWinner() != null){
            DiscColour winningColour = gameGridService.getGameWinner();
            Optional<Player> winnerOpt = playerService.getCurrentPlayers().stream()
                    .filter(player -> player.getDiscColour() == winningColour).findAny();
            winnerOpt.ifPresent(player ->
                    gameState.setWinningPlayer(new Player(player.getName(), winningColour)));
        }

        return gameState;
    }
}
