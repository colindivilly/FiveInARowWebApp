package genesys.service;

import genesys.GameProperties;
import genesys.GridProperties;
import genesys.model.DiscColour;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameRuleImplTest {

    @InjectMocks
    private GameRuleImpl gameRuleImpl;

    @Mock
    private GridProperties gridProperties;

    @Mock
    private GameProperties gameProperties;

    private final int columns = 9;
    private final int rows = 6;
    private final int goal = 5;

    @Before
    public void setUp(){
        when(gridProperties.getColumns()).thenReturn(columns);
        when(gridProperties.getRows()).thenReturn(rows);
        when(gameProperties.getDiscGoal()).thenReturn(goal);

        gameRuleImpl = new GameRuleImpl(gridProperties, gameProperties);
    }

    @Test
    public void ifTheGameGridIsEmptyThereIsNoWinner(){
        DiscColour[][] gameGrid = new DiscColour[rows][columns];

        assertFalse(gameRuleImpl.gameWinner(gameGrid).isPresent());
    }

    @Test
    public void ifTheGridContainsDiscsLessThanGoalForAtLeastOneColourThenThereIsNoWinner(){
        DiscColour[][] gameGrid = new DiscColour[rows][columns];

        //only 3 moves should not lead to a winner
        gameGrid[0] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red, DiscColour.Yellow, null, null, null, null, null, null};;

        assertFalse(gameRuleImpl.gameWinner(gameGrid).isPresent());
    }

    @Test
    public void ifThere5DiscsInARowVerticallyThereShouldBeAWinner(){
        Optional gameWinner;
        DiscColour[][] gameGrid = new DiscColour[rows][columns];

        gameGrid[0] = new DiscColour[]{DiscColour.Red, DiscColour.Red, DiscColour.Red,
                DiscColour.Red, null, null, null, null, null};
        gameGrid[1] = new DiscColour[]{DiscColour.Yellow, DiscColour.Yellow, DiscColour.Yellow,
                DiscColour.Yellow, DiscColour.Yellow, null, null, null, null};
        gameGrid[2] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};
        gameGrid[3] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};
        gameGrid[4] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};
        gameGrid[5] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};


        gameWinner = gameRuleImpl.gameWinner(gameGrid);
        assertTrue(gameWinner.isPresent());
        assertEquals(DiscColour.Yellow, gameWinner.get());

        gameGrid[0] = new DiscColour[]{DiscColour.Red, DiscColour.Red, DiscColour.Red,
                DiscColour.Red, DiscColour.Red, null, null, null, null};
        gameGrid[1] = new DiscColour[]{DiscColour.Yellow, DiscColour.Yellow, DiscColour.Yellow,
                DiscColour.Yellow, null, null, null, null, null};
        gameGrid[2] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};
        gameGrid[3] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};
        gameGrid[4] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};
        gameGrid[5] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};

        gameWinner = gameRuleImpl.gameWinner(gameGrid);
        assertTrue(gameWinner.isPresent());
        assertEquals(DiscColour.Red, gameWinner.get());
    }

    @Test
    public void ifThere5DiscsInARowHorizontallyThereShouldBeAWinner(){
        Optional gameWinner;
        DiscColour[][] gameGrid = new DiscColour[rows][columns];

        gameGrid[0] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red,
                null, null, null, null, null, null, null};
        gameGrid[1] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red,
                null, null, null, null, null, null, null};
        gameGrid[2] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red,
                null, null, null, null, null, null, null};
        gameGrid[3] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red,
                null, null, null, null, null, null, null};
        gameGrid[4] = new DiscColour[]{DiscColour.Yellow, null,
                null, null, null, null, null, null, null};
        gameGrid[5] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};

        gameWinner = gameRuleImpl.gameWinner(gameGrid);
        assertTrue(gameWinner.isPresent());
        assertEquals(DiscColour.Yellow, gameWinner.get());

        gameGrid[0] = new DiscColour[]{DiscColour.Red, DiscColour.Yellow,
                null, null, null, null, null, null, null};
        gameGrid[1] = new DiscColour[]{DiscColour.Red, DiscColour.Yellow,
                null, null, null, null, null, null, null};
        gameGrid[2] = new DiscColour[]{DiscColour.Red, DiscColour.Yellow,
                null, null, null, null, null, null, null};
        gameGrid[3] = new DiscColour[]{DiscColour.Red, DiscColour.Yellow,
                null, null, null, null, null, null, null};
        gameGrid[4] = new DiscColour[]{DiscColour.Red, null,
                null, null, null, null, null, null, null};
        gameGrid[5] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};

        gameWinner = gameRuleImpl.gameWinner(gameGrid);
        assertTrue(gameWinner.isPresent());
        assertEquals(DiscColour.Red, gameWinner.get());
    }

    @Test
    public void ifVerticalSequenceExistsThenReturnWinner(){
        DiscColour[][] gameGrid = new DiscColour[rows][columns];
        Optional gameWinner;

        gameGrid[0] = new DiscColour[]{DiscColour.Yellow, null,
                null, null, null, null, null, null, null};
        gameGrid[1] = new DiscColour[]{DiscColour.Red, DiscColour.Yellow,
                null, null, null, null, null, null, null};
        gameGrid[2] = new DiscColour[]{DiscColour.Red, DiscColour.Red,
                DiscColour.Yellow, null, null, null, null, null, null};
        gameGrid[3] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red,
                DiscColour.Yellow, DiscColour.Yellow, null, null, null, null, null};
        gameGrid[4] = new DiscColour[]{DiscColour.Yellow, DiscColour.Red,
                DiscColour.Red, DiscColour.Red, DiscColour.Yellow, null, null, null, null};
        gameGrid[5] = new DiscColour[]{null, null,
                null, null, null, null, null, null, null};

        gameWinner = gameRuleImpl.gameWinner(gameGrid);
        assertTrue(gameWinner.isPresent());
        assertEquals(DiscColour.Yellow, gameWinner.get());
    }

}