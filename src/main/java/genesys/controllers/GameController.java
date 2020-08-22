package genesys.controllers;

import genesys.model.Move;
import genesys.service.GameGridService;
import genesys.model.Player;
import genesys.service.GameStateService;
import genesys.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class GameController {

    Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameGridService gameGridService;

    @Autowired
    private GameStateService gameStateService;

    @PostMapping("/player")
    public ResponseEntity<Object> addPlayerName(@RequestBody Player player) throws GameRuleException {
        logger.info("Create new player with name " + player.getName() + " and colour " + player.getDiscColour());
        playerService.createNewPlayer(player);
        return new ResponseEntity<>("Player has been created", HttpStatus.CREATED);
    }

    @GetMapping("/players")
    public ResponseEntity<Object> getCurrentPlayers(){
        logger.info("Return players");
        return new ResponseEntity<>(playerService.getCurrentPlayers(), HttpStatus.OK);
    }

    @PostMapping("/restartgame")
    public ResponseEntity<Object> restartGame(){
        logger.info("Restart game");
        gameGridService.restartGame();
        return new ResponseEntity<>("Game has been restarted successfully", HttpStatus.CREATED);
   }

    @PostMapping("/move")
    public ResponseEntity<Object> addMove(@RequestBody Move move) throws GameRuleException {
        logger.info("Add new move", move.getColumn(), move.getDiscColour());
        gameGridService.addDisc(move);
        return new ResponseEntity<>("New Move added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/state")
    public ResponseEntity<Object> getGameState() {
        logger.info("Return current game state");
        return new ResponseEntity<>(gameStateService.getGameState(), HttpStatus.OK);
    }

    @GetMapping("/turn")
    public ResponseEntity<Object> whichColoursTurnIsIt(){
        logger.info("Return current turn details");
        return new ResponseEntity<>(playerService.whichPlayersTurnIsIt(gameGridService.whichColourHasTheNextTurn()), HttpStatus.OK);
    }
}
