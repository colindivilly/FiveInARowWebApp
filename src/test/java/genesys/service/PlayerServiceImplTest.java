package genesys.service;

import genesys.model.DiscColour;
import genesys.controllers.GameRuleException;
import genesys.model.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.junit.Assert.*;

public class PlayerServiceImplTest{

    private PlayerServiceImpl playerService;

    @Rule
    public ExpectedException gameRuleException = ExpectedException.none();

    @Before
    public void setUp(){
        playerService = new PlayerServiceImpl();
    }

    //test for function createNewPlayer

    @Test
    public void cannotAddMoreThanTwoPlayers() throws GameRuleException {
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Only Two players are allowed");

        Player player1 = new Player("player1", DiscColour.Red);
        Player player2 = new Player("player2", DiscColour.Yellow);
        playerService.createNewPlayer(player1);
        playerService.createNewPlayer(player2);

        Player player3 = new Player("player3", DiscColour.Red);
        playerService.createNewPlayer(player3);
    }

    @Test
    public void cannotAddDuplicatePlayerNames() throws GameRuleException{
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Cannot Duplicate players name");

        Player player1 = new Player("player1", DiscColour.Red);
        playerService.createNewPlayer(player1);

        Player player2 = new Player("player1", DiscColour.Yellow);
        playerService.createNewPlayer(player2);
    }

    @Test
    public void cannotAddDuplicateColourDisc() throws GameRuleException{
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Cannot Duplicate Colour");

        Player player1 = new Player("player1", DiscColour.Red);
        playerService.createNewPlayer(player1);

        Player player2 = new Player("player2", DiscColour.Red);
        playerService.createNewPlayer(player2);
    }

    @Test
    public void addPlayersShouldPutNewPlayerOnList() throws GameRuleException {
        Player player1 = new Player("player1", DiscColour.Yellow);
        playerService.createNewPlayer(player1);

        assertEquals(playerService.getCurrentPlayers().size(), 1);
        assertEquals(playerService.getCurrentPlayers().get(0), player1);

        Player player2 = new Player("player2", DiscColour.Red);
        playerService.createNewPlayer(player2);

        assertEquals(2, playerService.getCurrentPlayers().size());
        assertEquals(player2, playerService.getCurrentPlayers().get(1));
    }

    //test for function whichPlayersTurnIsIt

    @Test
    public void returnsEmptyOptionalIfNoPlayersHaveTheDiscColour() throws GameRuleException {
        Optional<Player> playerOptional;

        playerOptional = playerService.whichPlayersTurnIsIt(DiscColour.Yellow);

        assertFalse(playerOptional.isPresent());

        Player player1 = new Player("player1", DiscColour.Yellow);
        playerService.createNewPlayer(player1);

        playerOptional = playerService.whichPlayersTurnIsIt(DiscColour.Red);

        assertFalse(playerOptional.isPresent());
    }

    @Test
    public void returnPlayWhoHasTheColour() throws GameRuleException {
        Optional<Player> playerOptional;

        Player player1 = new Player("player1", DiscColour.Yellow);
        playerService.createNewPlayer(player1);
        Player player2 = new Player("player2", DiscColour.Red);
        playerService.createNewPlayer(player2);

        playerOptional = playerService.whichPlayersTurnIsIt(DiscColour.Red);

        assertTrue(playerOptional.isPresent());
        assertEquals(player2, playerOptional.get());
    }
}