package genesys.service;

import genesys.GameProperties;
import genesys.GridProperties;
import genesys.model.DiscColour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GameRuleImpl implements GameRule {

    @Autowired
    private final GridProperties gridProperties;

    @Autowired
    private final GameProperties gameProperties;

    private final StringBuilder yellowWinningPattern;
    private final StringBuilder redWinningPattern;

    public GameRuleImpl(GridProperties gridProperties, GameProperties gameProperties) {
        this.gridProperties = gridProperties;
        this.gameProperties = gameProperties;

        yellowWinningPattern = new StringBuilder();
        redWinningPattern = new StringBuilder();
        int i;
        for(i=0;i<gameProperties.getDiscGoal();i++){
            yellowWinningPattern.append(DiscColour.Yellow.toString());
            redWinningPattern.append(DiscColour.Red.toString());
        }
    }

    @Override
    public Optional<DiscColour> gameWinner(DiscColour[][] gameGrid) {

        AtomicInteger cellsWithDiscs = new AtomicInteger();
        AtomicInteger cellsWithYellow = new AtomicInteger();
        AtomicInteger cellsWithRed = new AtomicInteger();
        Stream.of(gameGrid)
                .flatMap(Stream::of)
                .forEach(cell -> {
                    if(cell != null){
                        cellsWithDiscs.set(cellsWithDiscs.get() + 1);

                        if(cell == DiscColour.Yellow){
                            cellsWithYellow.set(cellsWithYellow.get() + 1);
                        }else if(cell == DiscColour.Red){
                            cellsWithRed.set(cellsWithRed.get() + 1);
                        }
                    }
                });

        //at least five discs of one colour must be present in the
        // grid for there to be a winner
        if(cellsWithYellow.get() < gameProperties.getDiscGoal() &&
                cellsWithRed.get() < gameProperties.getDiscGoal() ){
            return Optional.empty();
        }

        // transform all the potential rows into a list of strings and then check strings for
        // a winning sequences

        // horizontal rows
        List<String> allPossibleSequences = new ArrayList<>(transformGridToStringRows(gameGrid));

        // vertical rows, rotate the existing grid
        int i;
        int j;
        DiscColour [][] rotateGrid = new DiscColour[gridProperties.getColumns()]
                [gridProperties.getRows()];
        for(i=0;i<gridProperties.getRows();i++){
            for(j=0;j<gridProperties.getColumns();j++){
                rotateGrid[j][i] = gameGrid[i][j];
            }
        }
        allPossibleSequences.addAll(transformGridToStringRows(rotateGrid));

        allPossibleSequences.addAll(createDiagonalSequence(gameGrid, gridProperties.getRows(),
                gridProperties.getColumns()));

        DiscColour [][] swapPositions = new DiscColour[gridProperties.getRows()]
                [gridProperties.getColumns()];

        for(i=0;i<gameGrid.length;i++){
            for(j=0;j<gameGrid[i].length;j++){

                swapPositions[(gameGrid.length-1)-i][(gameGrid[i].length-1)-j] = gameGrid[i][j];
            }
        }

        // maybe just reverse the starting poisitoon of the ir
        allPossibleSequences.addAll(createDiagonalSequence(swapPositions, gridProperties.getRows(),
                gridProperties.getColumns()));

        Optional<String> winningPattern = allPossibleSequences.stream().filter(row ->
                row.contains(yellowWinningPattern) || row.contains(redWinningPattern)).findAny();

        if(winningPattern.isPresent()){
            if(winningPattern.get().contains(yellowWinningPattern.toString())){
                return Optional.of(DiscColour.Yellow);
            }else{
                return Optional.of(DiscColour.Red);
            }
        }

        return Optional.empty();
    }

    private List<String> createDiagonalSequence(DiscColour [][] grid, int rows, int columns){
        int i;
        int j;
        List<String> rowAsString = new ArrayList<>();

        //initially add we can add as many cells in a diagonal that there
        // are rows starting at 0,0
        int cellsToAdd;
        StringBuilder rowAsText;

        cellsToAdd = Math.min(rows, columns);
        for(i=0;i<rows;i++){
            rowAsText = new StringBuilder();
            for(j=0;j<cellsToAdd;j++){
                rowAsText.append(grid[i+j][j]);
            }
            if(rows - i <= cellsToAdd){
                cellsToAdd = cellsToAdd - 1;
            }
            rowAsString.add(rowAsText.toString());
        }

        if(rows <= columns){
            cellsToAdd = rows -1;
        }else{
            cellsToAdd = columns - 1;
        }
        for(i=1;i<columns-1;i++){
            rowAsText = new StringBuilder();
            for (j=0;j<cellsToAdd;j++){
                rowAsText.append(grid[j][i+j]);
            }
            if(columns - i <= cellsToAdd){
                cellsToAdd = cellsToAdd - 1;
            }
            rowAsString.add(rowAsText.toString());
        }

        return rowAsString;
    }

    private List<String> transformGridToStringRows(DiscColour [][] gameGrid){
        return (Stream.of(gameGrid).map(row -> Stream.of(row).map(Objects::toString).
                collect(Collectors.joining())).collect(Collectors.toCollection(ArrayList::new)));
    }
}
