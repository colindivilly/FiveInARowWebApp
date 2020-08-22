package genesys.service;

import genesys.model.DiscColour;

import java.util.Optional;

public interface GameRule {
    public Optional<DiscColour> gameWinner(DiscColour[][] gameGrid);
}
