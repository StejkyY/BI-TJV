package cz.cvut.fit.stejsj27.service;

import cz.cvut.fit.stejsj27.dto.PlayerCreateDTO;
import cz.cvut.fit.stejsj27.dto.PlayerDTO;
import cz.cvut.fit.stejsj27.entity.Player;
import cz.cvut.fit.stejsj27.repository.PlayerRepository;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    void findAll() {

        Player playerOne = new Player( "Jan", "Stejskal");
        Player playerTwo = new Player( "Pepa", "Novák");
        Player playerThree = new Player( "Jarda", "Kudílek");

        ReflectionTestUtils.setField ( playerOne, "id", 5 );
        ReflectionTestUtils.setField ( playerTwo, "id", 6 );
        ReflectionTestUtils.setField ( playerThree, "id", 7 );

        PlayerDTO playerOneDTO = new PlayerDTO( playerOne.getId(), "Jan", "Stejskal");
        PlayerDTO playerTwoDTO = new PlayerDTO( playerTwo.getId(),"Pepa", "Novák");
        PlayerDTO playerThreeDTO = new PlayerDTO( playerThree.getId(), "Jarda", "Kudílek");

        List<Player> players = Arrays.asList( playerOne, playerTwo, playerThree );
        List<PlayerDTO> playersDTO = Arrays.asList( playerOneDTO, playerTwoDTO, playerThreeDTO );

        BDDMockito.given( playerRepository.findAll() ).willReturn( players );

        Assertions.assertEquals( playersDTO, playerService.findAll() );
        Mockito.verify( playerRepository, Mockito.atLeastOnce() ).findAll( );
    }

    @Test
    void create() throws Exception {
        Player player = new Player ( "Jan", "Stejskal" );

        PlayerDTO playerDTO = new PlayerDTO (player.getId(), player.getFirstName(), player.getLastName() );
        PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO( player.getFirstName(), player.getLastName() );

        BDDMockito.given ( playerRepository.save( player ) ).willReturn ( player );
        Assertions.assertEquals( playerDTO ,playerService.create( playerCreateDTO ));
        Mockito.verify( playerRepository, Mockito.atLeastOnce() ).save( player );
    }

    @Test
    void update() throws Exception {
        Player player = new Player ( "Jan", "Stejskal" );

        PlayerDTO playerDTO = new PlayerDTO (player.getId(), player.getFirstName(), player.getLastName() );
        PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO( player.getFirstName(), player.getLastName() );

        BDDMockito.given ( playerRepository.findById( player.getId()) ).willReturn ( Optional.of(player) );
        Assertions.assertEquals( playerDTO, playerService.update(player.getId(), playerCreateDTO ));
        Mockito.verify( playerRepository, Mockito.atLeastOnce() ).findById( player.getId());
    }

    @Test
    void deleteById() {
        playerRepository.deleteById( 1 );
        Mockito.verify( playerRepository, Mockito.atLeastOnce() ).deleteById( 1 );
    }

    @Test
    void findByIds() {
        Player playerOne = new Player( "Jan", "Stejskal");
        Player playerTwo = new Player( "Pepa", "Novák");
        Player playerThree = new Player( "Jarda", "Kudílek");
        ReflectionTestUtils.setField ( playerOne, "id", 6 );
        ReflectionTestUtils.setField ( playerTwo, "id", 7 );
        ReflectionTestUtils.setField ( playerThree, "id", 8 );

        List<Player> players = Arrays.asList( playerOne, playerTwo, playerThree );
        List<Integer> playerIds = Arrays.asList ( playerOne.getId(), playerTwo.getId(), playerThree.getId() );

        BDDMockito.given ( playerRepository.findAllById( playerIds )).willReturn( players );

        Assertions.assertEquals( players, playerService.findByIds( playerIds) );
        Mockito.verify ( playerRepository, Mockito.atLeastOnce() ).findAllById( playerIds );
    }

    @Test
    void findById() {
        Player playerOne = new Player( "Jan", "Stejskal");

        BDDMockito.given( playerRepository.findById( playerOne.getId() ) ).willReturn(Optional.of(playerOne));
        Assertions.assertEquals( Optional.of( playerOne), playerService.findById( playerOne.getId() ));

        Mockito.verify ( playerRepository, Mockito.atLeastOnce()).findById( playerOne.getId() );
    }

    @Test
    void findByIdAsDTO() {
        Player player = new Player ( "Jan", "Stejskal" );
        PlayerDTO playerOne = new PlayerDTO(player.getId(), "Jan", "Stejskal");

        BDDMockito.given( playerRepository.findById( player.getId() ) ).willReturn( Optional.of( player ) );
        Assertions.assertEquals( Optional.of( playerOne ), playerService.findByIdAsDTO( player.getId() ));

        Mockito.verify ( playerRepository, Mockito.atLeastOnce()).findById( player.getId() );
    }

    @Test
    void findByLastName() {
        Player playerOne = new Player( "Jan", "Novák");
        Player playerTwo = new Player( "Pepa", "Novák");
        Player playerThree = new Player( "Jarda", "Kudílek");

        ReflectionTestUtils.setField ( playerOne, "id", 5 );
        ReflectionTestUtils.setField ( playerTwo, "id", 6 );
        ReflectionTestUtils.setField ( playerThree, "id", 7 );

        PlayerDTO playerOneDTO = new PlayerDTO( playerOne.getId(), "Jan", "Novák");
        PlayerDTO playerTwoDTO = new PlayerDTO( playerTwo.getId(),"Pepa", "Novák");
        PlayerDTO playerThreeDTO = new PlayerDTO( playerThree.getId(), "Jarda", "Kudílek");

        List<Player> players = Arrays.asList( playerOne, playerTwo, playerThree );
        List<PlayerDTO> playersDTO = Arrays.asList( playerOneDTO, playerTwoDTO, playerThreeDTO );

        BDDMockito.given( playerRepository.findByLastName( playerOne.getLastName() ) ).willReturn( players );
        Assertions.assertEquals( playersDTO, playerService.findByLastName( playerOne.getLastName() ));

        Mockito.verify ( playerRepository, Mockito.atLeastOnce()).findByLastName( playerOne.getLastName() );
    }
}