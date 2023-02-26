package cz.cvut.fit.stejsj27.service;

import cz.cvut.fit.stejsj27.dto.TeamCreateDTO;
import cz.cvut.fit.stejsj27.dto.TeamDTO;
import cz.cvut.fit.stejsj27.entity.ExistingEntityException;
import cz.cvut.fit.stejsj27.entity.NonExistentEntityException;
import cz.cvut.fit.stejsj27.entity.Player;
import cz.cvut.fit.stejsj27.entity.Team;
import cz.cvut.fit.stejsj27.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerService playerService;

    @Autowired
    public TeamService(TeamRepository teamRepository, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    public List<TeamDTO> findAll ( )
    {
        List <TeamDTO> teams = teamRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        if (teams.isEmpty()) throw new NonExistentEntityException();
        return teams;
    }

    @Transactional
    public TeamDTO create (TeamCreateDTO teamCreateDTO ) throws NonExistentEntityException {
        List<Player> players = playerService.findByIds( teamCreateDTO.getPlayersIds() );

        if (players.size() != teamCreateDTO.getPlayersIds().size())
            throw new NonExistentEntityException();
        if ( teamRepository.findByName( teamCreateDTO.getName() ).isPresent() )
            throw new ExistingEntityException();
        return toDTO( teamRepository.save( new Team ( teamCreateDTO.getName(), teamCreateDTO.getMembersCount(), players ) ) );
    }

    @Transactional
    public TeamDTO update ( int id, TeamCreateDTO teamCreateDTO ) throws NonExistentEntityException {
        Optional <Team> optionalTeam = teamRepository.findById( id );
        if (optionalTeam.isEmpty())
            throw new NonExistentEntityException ();

        List<Player> players = playerService.findByIds( teamCreateDTO.getPlayersIds() );

        if (players.size() != teamCreateDTO.getPlayersIds().size())
            throw new NonExistentEntityException();

        Team team = optionalTeam.get();
        team.setName(teamCreateDTO.getName());
        team.setMembersCount( players.size() );
        team.setPlayers( players );
        return toDTO ( team );
    }

    @Transactional
    public void deleteById ( int id )
    {
        teamRepository.deleteById( id );
    }

    @Transactional
    public TeamDTO deleteAndMigratePlayersToDifferentTeam ( int id_delete, int id_migrate ) throws NonExistentEntityException {
        Optional < Team > optionalTeamD = teamRepository.findById( id_delete );
        if ( optionalTeamD.isEmpty() )
            throw new NonExistentEntityException( );

        Optional < Team > optionalTeamM = teamRepository.findById( id_migrate );
        if ( optionalTeamM.isEmpty() )
            throw new NonExistentEntityException( );

        Team migrationTeam = optionalTeamM.get();
        Team deleteTeam = optionalTeamD.get();
        List <Player> players = new ArrayList<>( migrationTeam.getPlayers() );
        for ( Player p : deleteTeam.getPlayers() )
        {
            if (!players.contains(p)) players.add( p );
        }

        deleteById( id_delete );
        migrationTeam.setMembersCount( players.size() );
        migrationTeam.setPlayers( players );

        return toDTO( migrationTeam );
    }

    public List<Team> findByIds ( List <Integer> ids )
    {
        return teamRepository.findAllById( ids );
    }

    public Optional<Team> findById ( int id )
    {
        return teamRepository.findById( id );
    }

    public Optional<TeamDTO> findByIdAsDTO (int id )
    {
        return toDTO ( teamRepository.findById( id ) );
    }

    public Optional <TeamDTO> findByName ( String name ) { return toDTO(teamRepository.findByName( name ) ); }

    public List <TeamDTO> findAllTeamsByMembersCount ( int membersCount )
    {
        return teamRepository.findAllTeamsByMembersCount( membersCount ).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List <TeamDTO> findAllTeamsByPlayerLastName ( String lastName )
    {
        return teamRepository.findAllTeamsByPlayerLastName( lastName ).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private TeamDTO toDTO ( Team team )
    {
        return new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), team.getPlayers().stream().map(Player::getId).collect(Collectors.toList()));
    }

    private Optional < TeamDTO > toDTO ( Optional <Team> optionalTeam )
    {
        if (optionalTeam.isEmpty())
            return Optional.empty();
        return Optional.of( toDTO( optionalTeam.get() ) );
    }
}
