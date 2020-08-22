package genesys.service;

import genesys.GridProperties;
import genesys.controllers.GameRuleException;
import genesys.model.DiscColour;
import genesys.model.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameGridServiceImpl implements GameGridService {
    @Autowired
    private final GridProperties gridProperties;

    @Autowired
    private final GameRule gameRule;

    private int turnNumber;
    private final int maxMoves;

    private DiscColour whichColoursTurnIsIt;
    //null means no winner yet or a draw
    private DiscColour gameWinner;

    private boolean isGameOver;

    private DiscColour[][] gameGrid;
    private int[] columnRowCount;

    Logger logger = LoggerFactory.getLogger(GameGridServiceImpl.class);

    public GameGridServiceImpl(GridProperties gridProperties, GameRule gameRule){
        this.gridProperties = gridProperties;
        this.gameRule = gameRule;

        maxMoves = gridProperties.getColumns() * gridProperties.getRows();

        createGameVariables();
    }

    private void createGameVariables(){
        gameGrid = new DiscColour[gridProperties.getRows()][gridProperties.getColumns()];
        columnRowCount = new int[gridProperties.getColumns()];

        // yellow goes first, according to the rules of the game on wikipedia
        whichColoursTurnIsIt = DiscColour.Yellow;
        turnNumber = 0;
        isGameOver = false;
    }

    @Override
    public void addDisc(Move move) throws GameRuleException {
        // check that the function parameters don't break some of the game rules
        if(move.getDiscColour() == null){
            throw new GameRuleException("Disc colour must be set");
        }else if(turnNumber == 0 && move.getDiscColour() != DiscColour.Yellow)
            throw new GameRuleException("First move must be made by yellow");
        else if(move.getDiscColour() != whichColoursTurnIsIt){
            throw new GameRuleException("Player cannot have two turns in a row");
        }else if(move == null || move.getColumn() >= gridProperties.getColumns() || move.getColumn() < 0){
            throw new GameRuleException("Invalid column number");
        }else if(columnRowCount[move.getColumn()] == gridProperties.getRows()){
            throw new GameRuleException("Column is full");
        }

        gameGrid[columnRowCount[move.getColumn()]][move.getColumn()] = move.getDiscColour();
        columnRowCount[move.getColumn()] = columnRowCount[move.getColumn()] + 1;

        //change turn after end of move
        if(move.getDiscColour() == DiscColour.Yellow){
            whichColoursTurnIsIt = DiscColour.Red;
        }else{
            whichColoursTurnIsIt = DiscColour.Yellow;
        }

        turnNumber = turnNumber + 1;
        logger.info("Number of turns taken in the game: " + turnNumber);

        Optional<DiscColour> winnerOptional = gameRule.gameWinner(gameGrid);

        if(winnerOptional.isPresent()){
            gameWinner = winnerOptional.get();
            isGameOver = true;
        }else if(turnNumber == maxMoves){
            isGameOver = true;
        }
    }

    @Override
    public DiscColour[][] getGameGrid() {
        return gameGrid;
    }

    @Override
    public DiscColour whichColourHasTheNextTurn() {
        return whichColoursTurnIsIt;
    }

    @Override
    public DiscColour getGameWinner() {
        return gameWinner;
    }

    @Override
    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public void restartGame(){
        createGameVariables();
    }

}