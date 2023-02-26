package cz.cvut.fit.stejsj27.service;

import cz.cvut.fit.stejsj27.dto.*;
import cz.cvut.fit.stejsj27.dto.ClubDTO;
import cz.cvut.fit.stejsj27.entity.*;
import cz.cvut.fit.stejsj27.entity.Club;
import cz.cvut.fit.stejsj27.entity.Club;
import cz.cvut.fit.stejsj27.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final TeamService teamService;

    @Autowired
    public ClubService(ClubRepository clubRepository, TeamService teamService) {
        this.clubRepository = clubRepository;
        this.teamService = teamService;
    }

    @Transactional
    public ClubDTO create (ClubCreateDTO clubCreateDTO ) throws NonExistentEntityException {
        if ( clubRepository.findByName(clubCreateDTO.getName()).isPresent() )
            throw new ExistingEntityException();
        List<Team> teams = teamService.findByIds( clubCreateDTO.getTeamsIds() );
        if (teams.size() != clubCreateDTO.getTeamsIds().size())
            throw new NonExistentEntityException( );

        return toDTO( clubRepository.save( new Club ( clubCreateDTO.getName(), teams ) ) );
    }

    @Transactional
    public ClubDTO update ( int id, ClubCreateDTO clubCreateDTO ) throws NonExistentEntityException {
        Optional <Club> optionalClub = clubRepository.findById( id );
        if (optionalClub.isEmpty())
            throw new NonExistentEntityException ( );

        List<Team> teams = teamService.findByIds( clubCreateDTO.getTeamsIds() );

        if (teams.size() != clubCreateDTO.getTeamsIds().size())
            throw new NonExistentEntityException( );

        Club club = optionalClub.get();
        club.setName(clubCreateDTO.getName());
        club.setTeams( teams );
        return toDTO ( club );
    }

    @Transactional
    public void deleteById ( int id )
    {
        clubRepository.deleteById( id );
    }

    public List<ClubDTO> findAll ( )
    {
        List <ClubDTO> clubs = clubRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        if ( clubs.isEmpty() ) throw new NonExistentEntityException();
        return clubs;
    }
    
    public List<Club> findByIds ( List <Integer> ids )
    {
        return clubRepository.findAllById( ids );
    }

    public Optional<Club> findById (int id )
    {
        return clubRepository.findById( id );
    }

    public Optional<ClubDTO> findByIdAsDTO (int id )
    {
        return toDTO ( clubRepository.findById( id ) );
    }

    public Optional <ClubDTO> findByName (String name ) { return toDTO( clubRepository.findByName( name ) ); }

    private ClubDTO toDTO (Club club )
    {
        return new ClubDTO( club.getId(), club.getName(), club.getTeams().stream().map(Team::getId).collect(Collectors.toList()) );
    }

    private Optional < ClubDTO > toDTO ( Optional <Club> optionalClub )
    {
        if (optionalClub.isEmpty())
            return Optional.empty();
        return Optional.of( toDTO( optionalClub.get() ) );
    }

}
