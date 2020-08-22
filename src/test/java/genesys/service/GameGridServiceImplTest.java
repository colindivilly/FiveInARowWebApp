package genesys.service;

import genesys.GridProperties;
import genesys.controllers.GameRuleException;
import genesys.model.DiscColour;
import genesys.model.Move;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameGridServiceImplTest {

    @InjectMocks
    private GameGridServiceImpl gameGridService;

    @Mock
    private GridProperties gridProperties;

    @Mock
    private GameRuleImpl gameRule;

    @Rule
    public ExpectedException gameRuleException = ExpectedException.none();

    @Before
    public void setUp(){
        when(gridProperties.getColumns()).thenReturn(9);
        when(gridProperties.getRows()).thenReturn(6);
        when(gameRule.gameWinner(any())).thenReturn(Optional.empty());
        gameGridService = new GameGridServiceImpl(gridProperties, gameRule);
    }

    @Test
    public void ifDiscColourIsNullGameRulesAreBroken() throws GameRuleException {
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Disc colour must be set");

        gameGridService.addDisc(new Move(null, 0));
    }

    @Test
    public void yellowShouldBeTheFirstColouredDiscAddedToTheGrid() throws GameRuleException{
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("First move must be made by yellow");

        gameGridService.addDisc(new Move(DiscColour.Red, 0));
    }

    @Test
    public void playersCannotHaveTwoTurnsInARow() throws GameRuleException{
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Player cannot have two turns in a row");

        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));
        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));
    }

    @Test
    public void playerCannotAddDiscToAColumnThatDoesNotExist() throws GameRuleException{
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Invalid column number");

        gameGridService.addDisc(new Move(DiscColour.Yellow, 9));
    }

    @Test
    public void playerCannotAddDiscToFullColumn() throws GameRuleException{
        gameRuleException.expect(GameRuleException.class);
        gameRuleException.expectMessage("Column is full");

        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));
        gameGridService.addDisc(new Move(DiscColour.Red, 0));
        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));
        gameGridService.addDisc(new Move(DiscColour.Red, 0));
        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));
        gameGridService.addDisc(new Move(DiscColour.Red, 0));
        verify(gameRule, times(6)).gameWinner(any());

        // exception should now be thrown as here are only 6 rows in the game grid
        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));
    }

    @Test
    public void addDiscToGrid() throws GameRuleException{
        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));

        assertEquals(DiscColour.Yellow, gameGridService.getGameGrid()[0][0]);
        assertNull(null, gameGridService.getGameGrid()[1][0]);
        assertNull(null, gameGridService.getGameGrid()[2][0]);
        assertNull(gameGridService.getGameGrid()[3][0]);
        assertNull(null, gameGridService.getGameGrid()[4][0]);
        assertNull(null, gameGridService.getGameGrid()[5][0]);
        verify(gameRule, times(1)).gameWinner(any());


        gameGridService.addDisc(new Move(DiscColour.Red, 0));

        assertEquals(DiscColour.Yellow, gameGridService.getGameGrid()[0][0]);
        assertEquals(DiscColour.Red, gameGridService.getGameGrid()[1][0]);
        assertNull(null, gameGridService.getGameGrid()[2][0]);
        assertNull(null, gameGridService.getGameGrid()[3][0]);
        assertNull(null, gameGridService.getGameGrid()[4][0]);
        assertNull(null, gameGridService.getGameGrid()[5][0]);
        verify(gameRule, times(2)).gameWinner(any());

        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));

        assertEquals(DiscColour.Yellow, gameGridService.getGameGrid()[0][0]);
        assertEquals(DiscColour.Red, gameGridService.getGameGrid()[1][0]);
        assertEquals(DiscColour.Yellow, gameGridService.getGameGrid()[2][0]);
        assertNull(null, gameGridService.getGameGrid()[3][0]);
        assertNull(null, gameGridService.getGameGrid()[4][0]);
        assertNull(null, gameGridService.getGameGrid()[5][0]);
        verify(gameRule, times(3)).gameWinner(any());
    }

    @Test
    public void ifMoveLeadsToGameOverStateUpdateFlags() throws GameRuleException {
        gameGridService.addDisc(new Move(DiscColour.Yellow, 0));

        verify(gameRule, times(1)).gameWinner(any());
        assertNull(gameGridService.getGameWinner());
        assertFalse(gameGridService.isGameOver());

        when(gameRule.gameWinner(any())).thenReturn(Optional.of(DiscColour.Red));

        gameGridService.addDisc(new Move(DiscColour.Red, 0));

        assertEquals(gameGridService.getGameWinner(),DiscColour.Red);
        assertTrue(gameGridService.isGameOver());
        verify(gameRule, times(2)).gameWinner(any());
    }
}