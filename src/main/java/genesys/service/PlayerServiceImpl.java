package genesys.service;

import genesys.model.DiscColour;
import genesys.controllers.GameRuleException;
import genesys.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private List<Player> players;

    public PlayerServiceImpl() {
        this.players = new ArrayList<>();
    }

    @Override
    public void createNewPlayer(Player player) throws GameRuleException {

        if(players.size() == 2){
            throw new GameRuleException("Only Two players are allowed");
        }else if(players.size() == 1){

            Player player1 = players.get(0);
            if(player1.getName() == player.getName()){
                throw new GameRuleException("Cannot Duplicate players name");
            }else if(player1.getDiscColour() == player.getDiscColour()){
                throw new GameRuleException("Cannot Duplicate Colour");
            }
        }

        players.add(player);
    }

    @Override
    public Optional<Player> whichPlayersTurnIsIt(DiscColour coloursTurn) {
        return players.stream().filter(p ->{
            return p.getDiscColour() == coloursTurn;
        }).findAny();
    }

    @Override
    public List<Player> getCurrentPlayers() {
        return players;
    }
}
