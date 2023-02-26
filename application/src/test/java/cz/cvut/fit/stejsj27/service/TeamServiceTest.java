package cz.cvut.fit.stejsj27.service;

import cz.cvut.fit.stejsj27.dto.ClubDTO;
import cz.cvut.fit.stejsj27.dto.TeamCreateDTO;
import cz.cvut.fit.stejsj27.dto.TeamDTO;
import cz.cvut.fit.stejsj27.entity.Player;
import cz.cvut.fit.stejsj27.entity.Team;
import cz.cvut.fit.stejsj27.repository.TeamRepository;
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
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private PlayerService playerService;

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
        List<Integer> playersIds = Arrays.asList( player.getId() );
        List<Integer> playersIds2 = Arrays.asList( player2.getId(), player3.getId() );

        Team team = new Team ( "Slavoj A", players.size(), players );
        Team team2 = new Team ( "Spartak A", players2.size(), players2 );
        ReflectionTestUtils.setField ( team, "id", 8 );
        ReflectionTestUtils.setField ( team2, "id", 9 );
        List<Team> teams = Arrays.asList( team, team2 );
        List<Integer> teamsIds = Arrays.asList( team.getId(), team2.getId() );

        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );
        TeamDTO teamDTO2 = new TeamDTO( team2.getId(), team2.getName(), team2.getMembersCount(), playersIds2 );
        List <TeamDTO> teamsDTO = Arrays.asList( teamDTO, teamDTO2 );

        BDDMockito.given( teamRepository.findAll( ) ).willReturn(  teams );
        Assertions.assertEquals( teamsDTO , teamService.findAll( ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findAll( );
    }

    @Test
    void create() throws Exception {
        Player player = new Player( "Jan", "Stejskal" );

        List<Player> players = Arrays.asList( player );
        List<Integer> playersIds = Arrays.asList( player.getId() );

        Team team = new Team ( "Slavoj A", players.size(), players );
        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO( team.getName(), team.getMembersCount(), playersIds );

        BDDMockito.given ( teamRepository.save( team ) ).willReturn( team );
        BDDMockito.given ( playerService.findByIds( teamCreateDTO.getPlayersIds() ) ).willReturn( players );

        Assertions.assertEquals( teamDTO , teamService.create( teamCreateDTO ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findByName( team.getName() );
        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).save( team );
        Mockito.verify( playerService, Mockito.atLeastOnce() ).findByIds( teamCreateDTO.getPlayersIds() );
    }

    @Test
    void update() throws Exception {
        Player player = new Player( "Jan", "Stejskal" );

        List<Player> players = Arrays.asList( player );
        List<Integer> playersIds = Arrays.asList( player.getId() );

        Team team = new Team ( "Slavoj A", players.size(), players );
        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO( team.getName(), team.getMembersCount(), playersIds );

        BDDMockito.given ( teamRepository.findById( team.getId() ) ).willReturn(Optional.of(team));
        BDDMockito.given ( playerService.findByIds( teamCreateDTO.getPlayersIds() ) ).willReturn( players );

        Assertions.assertEquals( teamDTO , teamService.update(team.getId(), teamCreateDTO ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findById( team.getId() );
        Mockito.verify( playerService, Mockito.atLeastOnce() ).findByIds( teamCreateDTO.getPlayersIds() );
    }

    @Test
    void deleteById() {
        teamRepository.deleteById( 1 );
        Mockito.verify( teamRepository, Mockito.atLeastOnce() ).deleteById( 1 );
    }

    @Test
    void deleteAndMigratePlayersToDifferentTeam () throws Exception {
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
        ReflectionTestUtils.setField ( team, "id", 11 );
        ReflectionTestUtils.setField ( team2, "id", 12 );

        List<Integer> playersIds = Arrays.asList( player2.getId(), player3.getId(), player.getId() );
        TeamDTO verifyTeam = new TeamDTO( team2.getId(), team2.getName(), team.getMembersCount() + team2.getMembersCount(),
                                                    playersIds );

        BDDMockito.given ( teamRepository.findById( team.getId() ) ).willReturn(Optional.of(team));
        BDDMockito.given ( teamRepository.findById( team2.getId() ) ).willReturn(Optional.of(team2));
        Assertions.assertEquals( verifyTeam, teamService.deleteAndMigratePlayersToDifferentTeam(team.getId(), team2.getId() ) );

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findById( team.getId() );
        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findById( team2.getId() );
        Mockito.verify( teamRepository, Mockito.atLeastOnce() ).deleteById( team.getId() );
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

        List<Team> teams = Arrays.asList( team, team2 );
        List<Integer> teamsIds = Arrays.asList( team.getId(), team2.getId() );

        BDDMockito.given( teamRepository.findAllById( teamsIds ) ).willReturn(  teams );
        Assertions.assertEquals( teams , teamService.findByIds( teamsIds ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findAllById( teamsIds );
    }

    @Test
    void findById() {
        Player player = new Player( "Jan", "Stejskal" );

        List<Player> players = Arrays.asList( player );
        Team team = new Team ( "Slavoj A", players.size(), players );

        BDDMockito.given( teamRepository.findById( team.getId() ) ).willReturn(Optional.of(team));
        Assertions.assertEquals( Optional.of( team ), teamService.findById( team.getId() ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce()).findById( team.getId() );
    }

    @Test
    void findByIdAsDTO() {
        Player player = new Player( "Jan", "Stejskal" );

        List<Player> players = Arrays.asList( player );
        List<Integer> playersIds = Arrays.asList( player.getId() );
        Team team = new Team ( "Slavoj A", players.size(), players );
        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );

        BDDMockito.given( teamRepository.findById( team.getId() ) ).willReturn(Optional.of(team));
        Assertions.assertEquals( Optional.of( teamDTO ), teamService.findByIdAsDTO( team.getId() ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce()).findById( team.getId() );
    }

    @Test
    void findByName() {
        Player player = new Player( "Jan", "Stejskal" );

        List<Player> players = Arrays.asList( player );
        List<Integer> playersIds = Arrays.asList( player.getId() );
        Team team = new Team ( "Slavoj A", players.size(), players );
        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );

        BDDMockito.given( teamRepository.findByName( team.getName() ) ).willReturn(Optional.of(team));
        Assertions.assertEquals( Optional.of( teamDTO ), teamService.findByName( team.getName() ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce()).findByName( team.getName() );
    }

    @Test
    void findAllTeamsByMembersCount() {
        Player player = new Player( "Jan", "Stejskal" );
        Player player2 = new Player ( "Pepa", "Novák" );
        ReflectionTestUtils.setField ( player, "id", 8 );
        ReflectionTestUtils.setField ( player2, "id", 9 );
        List<Player> players = Arrays.asList( player );
        List<Player> players2 = Arrays.asList( player2 );

        Team team = new Team ( "Slavoj A", players.size(), players );
        Team team2 = new Team ( "Spartak A", players2.size(), players2 );
        ReflectionTestUtils.setField ( team, "id", 8 );
        ReflectionTestUtils.setField ( team2, "id", 9 );
        List<Team> teams = Arrays.asList( team, team2 );

        List<Integer> playersIds = Arrays.asList( player.getId() );
        List<Integer> playersIds2 = Arrays.asList( player2.getId() );

        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );
        TeamDTO teamDTO2 = new TeamDTO( team2.getId(), team2.getName(), team2.getMembersCount(), playersIds2 );
        List <TeamDTO> teamsDTO = Arrays.asList( teamDTO, teamDTO2 );

        BDDMockito.given( teamRepository.findAllTeamsByMembersCount( team.getMembersCount() ) ).willReturn( teams );
        Assertions.assertEquals( teamsDTO , teamService.findAllTeamsByMembersCount(team.getMembersCount() ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findAllTeamsByMembersCount( team.getMembersCount() );
    }

    @Test
    void findAllTeamsByPlayerLastName() {
        Player player = new Player( "martin", "Novák" );
        Player player2 = new Player ( "Pepa", "Novák" );
        ReflectionTestUtils.setField ( player, "id", 8 );
        ReflectionTestUtils.setField ( player2, "id", 9 );
        List<Player> players = Arrays.asList( player );
        List<Player> players2 = Arrays.asList( player2 );
        List<Integer> playersIds = Arrays.asList( player.getId());
        List<Integer> playersIds2 = Arrays.asList( player2.getId());

        Team team = new Team ( "Slavoj A", players.size(), players );
        Team team2 = new Team ( "Slavoj B", players2.size(), players2 );
        ReflectionTestUtils.setField ( team, "id", 8 );
        ReflectionTestUtils.setField ( team2, "id", 9 );
        List<Team> teams = Arrays.asList( team, team2 );

        TeamDTO teamDTO = new TeamDTO( team.getId(), team.getName(), team.getMembersCount(), playersIds );
        TeamDTO teamDTO2 = new TeamDTO( team2.getId(), team2.getName(), team2.getMembersCount(), playersIds2 );
        List <TeamDTO> teamsDTO = Arrays.asList( teamDTO, teamDTO2 );

        BDDMockito.given( teamRepository.findAllTeamsByPlayerLastName( player2.getLastName() )).willReturn( teams );
        Assertions.assertEquals( teamsDTO , teamService.findAllTeamsByPlayerLastName( player2.getLastName() ));

        Mockito.verify ( teamRepository, Mockito.atLeastOnce() ).findAllTeamsByPlayerLastName( player2.getLastName() );
    }
}