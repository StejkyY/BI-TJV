package cz.cvut.fit.stejsj27.service;

import cz.cvut.fit.stejsj27.dto.ClubDTO;
import cz.cvut.fit.stejsj27.dto.PlayerCreateDTO;
import cz.cvut.fit.stejsj27.dto.PlayerDTO;
import cz.cvut.fit.stejsj27.dto.TeamDTO;
import cz.cvut.fit.stejsj27.entity.NonExistentEntityException;
import cz.cvut.fit.stejsj27.entity.Player;
import cz.cvut.fit.stejsj27.entity.Team;
import cz.cvut.fit.stejsj27.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerDTO> findAll ( )
    {
        List <PlayerDTO> players = playerRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        if ( players.isEmpty() ) throw new NonExistentEntityException();
        return players;
    }

    @Transactional
    public PlayerDTO create (PlayerCreateDTO playerCreateDTO )  {
        return toDTO(
                playerRepository
                        .save( new Player (playerCreateDTO.getFirstName(), playerCreateDTO.getLastName() ) ) );
    }

    @Transactional
    public PlayerDTO update ( int id, PlayerCreateDTO playerCreateDTO ) throws NonExistentEntityException {
        Optional <Player> optionalPlayer = findById( id );
        if ( optionalPlayer.isEmpty())
            throw new NonExistentEntityException();

        Player player = optionalPlayer.get();

        player.setFirstName(playerCreateDTO.getFirstName());
        player.setLastName(playerCreateDTO.getLastName());

        return toDTO ( player );
    }

    @Transactional
    public void deleteById ( int id )
    {
        playerRepository.findById( id ).orElseThrow( () -> new NonExistentEntityException() );
        playerRepository.deleteById( id );
    }

    public List<Player> findByIds (List <Integer> ids )
    {
        return playerRepository.findAllById( ids );
    }

    public Optional<Player> findById ( int id )
    {
        return playerRepository.findById( id );
    }

    public Optional<PlayerDTO> findByIdAsDTO (int id )
    {
        return toDTO ( playerRepository.findById( id ) );
    }

    public List < PlayerDTO > findByLastName (String lastName ) { return playerRepository.findByLastName( lastName )
            .stream().map( this::toDTO ).collect( Collectors.toList()); }

    private PlayerDTO toDTO ( Player player )
    {
        return new PlayerDTO( player.getId(), player.getFirstName(), player.getLastName() );
    }

    private Optional < PlayerDTO > toDTO ( Optional <Player> optionalPlayer )
    {
        if (optionalPlayer.isEmpty())
            return Optional.empty();
        return Optional.of( toDTO( optionalPlayer.get() ) );
    }
}
