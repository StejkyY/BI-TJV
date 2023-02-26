package cz.cvut.fit.stejsj27.service;

import cz.cvut.fit.stejsj27.dto.ClubCreateDTO;
import cz.cvut.fit.stejsj27.dto.ClubDTO;
import cz.cvut.fit.stejsj27.entity.Club;
import cz.cvut.fit.stejsj27.entity.Player;
import cz.cvut.fit.stejsj27.entity.Team;
import cz.cvut.fit.stejsj27.repository.ClubRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubServiceTest {

    @Autowired
    private ClubService clubService;

    @MockBean
    private ClubRepository clubRepository;

    @MockBean
    private TeamService teamService;

    @Test
    void create() throws Exception {
        Player player = new Player( "Jan", "Stejskal" );
        List<Player> players = Arrays.asList( player );
        Team team = new Team ( "Slavoj A", players.size(), players );
        List<Team> teams = Arrays.asList( team );
        List <Integer> teamsIds = Arrays.asList( team.getId() );

        Club club = new Club( "Slavoj Pacov", teams );
        ClubDTO clubDTO = new ClubDTO ( club.getId(), club.getName(), teamsIds );
        ClubCreateDTO clubCreateDTO = new ClubCreateDTO(club.getName(), teamsIds );

        BDDMockito.given ( clubRepository.save( club ) ).willReturn( club );
        BDDMockito.given ( teamService.findByIds( clubCreateDTO.getTeamsIds() ) ).willReturn( teams );

        Assertions.assertEquals( clubDTO , clubService.create( clubCreateDTO ));

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findByName(club.getName());
        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).save( club );
        Mockito.verify( teamService, Mockito.atLeastOnce() ).findByIds( clubCreateDTO.getTeamsIds() );
    }

    @Test
    void update() throws Exception {
        Player player = new Player( "Jan", "Stejskal" );
        List<Player> players = Arrays.asList( player );
        Team team = new Team ( "Slavoj A", players.size(), players );
        List<Team> teams = Arrays.asList( team );
        List <Integer> teamsIds = Arrays.asList( team.getId() );

        Club club = new Club( "Slavoj Pacov", teams );
        ClubDTO clubDTO = new ClubDTO ( club.getId(), club.getName(), teamsIds );
        ClubCreateDTO clubCreateDTO = new ClubCreateDTO(club.getName(), teamsIds );

        BDDMockito.given ( clubRepository.findById( club.getId() ) ).willReturn( Optional.of( club ) );
        BDDMockito.given ( teamService.findByIds( clubCreateDTO.getTeamsIds() ) ).willReturn( teams );

        Assertions.assertEquals( clubDTO , clubService.update( club.getId(),clubCreateDTO ));

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findById( club.getId() );
        Mockito.verify( teamService, Mockito.atLeastOnce() ).findByIds( clubCreateDTO.getTeamsIds() );
    }

    @Test
    void deleteById() {
        clubRepository.deleteById( 1 );
        Mockito.verify( clubRepository, Mockito.atLeastOnce() ).deleteById( 1 );
    }

    @Test
    void findAll() {
        Player player = new Player( "Jan", "Stejskal" );
        Player player2 = new Player ( "Pepa", "Novák" );
        Player player3 = new Player ( "Jarda", "Kudílek" );
        ReflectionTestUtils.setField ( player, "id", 8 );
        ReflectionTestUtils.setField ( player2, "id", 9 );
        ReflectionTestUtils.setField ( player3, "id", 10 );

        List<Player> players = Arrays.asList( player );
        List<Player> players2 = Arrays.asList( player2, player3 );
        Team team = new Team ( "Slavoj A", players.size(), players );
        Team team2 = new Team ( "Spartak A", players2.size(), players2 );
        ReflectionTestUtils.setField ( team, "id", 8 );
        ReflectionTestUtils.setField ( team2, "id", 9 );

        List<Team> teams = Arrays.asList( team );
        List<Team> teams2 = Arrays.asList( team2 );
        List <Integer> teamsIds = Arrays.asList( team.getId() );
        List <Integer> teamsIds2 = Arrays.asList( team2.getId() );

        Club club = new Club( "Slavoj Pacov", teams );
        Club club2 = new Club( "Slavoj Pelhřimov", teams2 );
        ReflectionTestUtils.setField ( club, "id", 8 );
        ReflectionTestUtils.setField ( club2, "id", 9 );
        ClubDTO clubDTO = new ClubDTO ( club.getId(), club.getName(), teamsIds );
        ClubDTO clubDTO2 = new ClubDTO ( club2.getId(), club2.getName(), teamsIds2 );

        List <Club> clubs = Arrays.asList( club, club2 );
        List <ClubDTO> clubsDTO = Arrays.asList( clubDTO, clubDTO2 );

        BDDMockito.given( clubRepository.findAll( ) ).willReturn(  clubs );
        Assertions.assertEquals( clubsDTO , clubService.findAll( ));

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findAll(  );
    }

    @Test
    void findByIds() {
        Player player = new Player( "Jan", "Stejskal" );
        Player player2 = new Player ( "Pepa", "Novák" );
        Player player3 = new Player ( "Jarda", "Kudílek" );
        ReflectionTestUtils.setField ( player, "id", 8 );
        ReflectionTestUtils.setField ( player2, "id", 9 );
        ReflectionTestUtils.setField ( player3, "id", 10 );

        List<Player> players = Arrays.asList( player );
        List<Player> players2 = Arrays.asList( player2, player3 );
        Team team = new Team ( "Slavoj A", players.size(), players );
        Team team2 = new Team ( "Spartak A", players2.size(), players2 );
        ReflectionTestUtils.setField ( team, "id", 8 );
        ReflectionTestUtils.setField ( team2, "id", 9 );
        List<Team> teams = Arrays.asList( team );
        List<Team> teams2 = Arrays.asList( team2 );

        Club club = new Club( "Slavoj Pacov", teams );
        Club club2 = new Club( "Slavoj Pelhřimov", teams2 );
        ReflectionTestUtils.setField ( club, "id", 8 );
        ReflectionTestUtils.setField ( club2, "id", 9 );
        List <Club> clubs = Arrays.asList( club, club2 );
        List <Integer> clubsIds = Arrays.asList( club.getId(), club2.getId() );

        BDDMockito.given( clubRepository.findAllById( clubsIds ) ).willReturn(  clubs );
        Assertions.assertEquals( clubs , clubService.findByIds( clubsIds ));

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findAllById( clubsIds );
    }

    @Test
    void findById() {
        Player player = new Player( "Jan", "Stejskal" );
        List<Player> players = Arrays.asList( player );
        Team team = new Team ( "Slavoj A", players.size(), players );
        List<Team> teams = Arrays.asList( team );
        Club club = new Club( "Slavoj Pacov", teams );

        BDDMockito.given( clubRepository.findById(club.getId()) ).willReturn( Optional.of ( club ) );
        Assertions.assertEquals( Optional.of ( club ), clubService.findById( club.getId() ) );

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findById( club.getId() );
    }

    @Test
    void findByIdAsDTO() {
        Player player = new Player( "Jan", "Stejskal" );
        List<Player> players = Arrays.asList( player );
        Team team = new Team ( "Slavoj A", 1, players );
        List<Team> teams = Arrays.asList( team );
        List<Integer> teamIds = Arrays.asList( team.getId() );
        Club club = new Club( "Slavoj Pacov", teams );
        ClubDTO clubDTO = new ClubDTO( club.getId(), club.getName(), teamIds );

        BDDMockito.given( clubRepository.findById(club.getId()) ).willReturn( Optional.of ( club ) );
        Assertions.assertEquals( Optional.of ( clubDTO ), clubService.findByIdAsDTO( club.getId() ) );

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findById( club.getId() );
    }

    @Test
    void findByName() {
        Player player = new Player( "Jan", "Stejskal" );
        List<Player> players = Arrays.asList( player );
        Team team = new Team ( "Slavoj A", 1, players );
        List<Team> teams = Arrays.asList( team );
        List<Integer> teamIds = Arrays.asList( team.getId() );
        Club club = new Club( "Slavoj Pacov", teams );
        ClubDTO clubDTO = new ClubDTO( club.getId(), club.getName(), teamIds );

        BDDMockito.given( clubRepository.findByName(club.getName()) ).willReturn( Optional.of ( club ) );
        Assertions.assertEquals( Optional.of ( clubDTO ), clubService.findByName( club.getName() ) );

        Mockito.verify ( clubRepository, Mockito.atLeastOnce() ).findByName( club.getName() );
    }
}