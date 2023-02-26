package cz.cvut.fit.stejsj27.semestralka_klient;

import cz.cvut.fit.stejsj27.semestralka_klient.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableHypermediaSupport( type = EnableHypermediaSupport.HypermediaType.HAL )
public class SemestralkaKlientApplication implements ApplicationRunner  {

    @Autowired
    private PlayerResource playerResource;

    @Autowired
    private ClubResource clubResource;

    @Autowired
    private TeamResource teamResource;

    public static void main(String[] args) {
        SpringApplication.run(SemestralkaKlientApplication.class, args);
    }

    @Bean
    RestTemplateCustomizer customizer ( HypermediaRestTemplateConfigurer c )
    {
        return restTemplate -> { c.registerHypermediaTypes( restTemplate ); };
    }

    @Override
    public void run(ApplicationArguments args) {
        if ( args.containsOption("app.action") && args.containsOption("app.entity"))
        {
            if ( args.getOptionValues( "app.entity" ).contains( "player" ) )
            {
                if ( args.getOptionValues( "app.action" ).contains( "createAndRead" ) ) {
                    if ( !args.containsOption("app.firstName") || !args.containsOption("app.lastName"))
                    {
                        System.err.println("Missing firstName or lastName");
                        return;
                    }
                    String firstName = args.getOptionValues("app.firstName").get(0);
                    String lastName = args.getOptionValues("app.lastName").get(0);
                    PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO(firstName, lastName);

                    playerResource.create (playerCreateDTO);
                    System.out.println( "succesfully created" );
                }
                if ( args.getOptionValues( "app.action" ).contains( "update" ) ) {
                    if ( !args.containsOption("app.firstName") || !args.containsOption("app.lastName")
                            || !args.containsOption("app.id"))
                    {
                        System.err.println("Missing firstName or lastName or id");
                        return;
                    }
                    String firstName = args.getOptionValues("app.firstName").get(0);
                    String lastName = args.getOptionValues("app.lastName").get(0);
                    int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                    PlayerCreateDTO playerCreateDTO = new PlayerCreateDTO(firstName, lastName);
                    try {
                        playerResource.update ( id ,playerCreateDTO);
                        System.out.println( "Successfully updated");
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                            System.err.println("Not Found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readById" ) ) {
                    if (!args.containsOption("app.id")) {
                        System.err.println("id is missing");
                        return;
                    }
                    try {
                        int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                        System.out.println( playerResource.readById(id).getFirstName( ) );
                        System.out.println( playerResource.readById(id).getLastName( ) );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Not found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readByPlayerLastName" ) ) {
                    if (!args.containsOption("app.lastName")) {
                        System.err.println("lastName is missing");
                        return;
                    }
                    List<PlayerDTO> playersReceived;
                    try {
                        String lastName = args.getOptionValues("app.lastName").get(0);
                        playersReceived = playerResource.readByPlayerLastName( lastName );
                        for ( PlayerDTO p : playersReceived )
                        {
                            System.out.println( p.getId() );
                            System.out.println( p.getFirstName() );
                            System.out.println( p.getLastName() );
                            System.out.println( "***********************************************" );
                        }
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Not found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readAll" ) ) {
                    try {
                        List<PlayerDTO> playersReceived;
                        playersReceived = playerResource.readAll();

                        for (PlayerDTO p : playersReceived) {
                            System.out.println(p.getId());
                            System.out.println(p.getFirstName());
                            System.out.println(p.getLastName());
                            System.out.println("***********************************************");
                        }
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Could not find any players");
                    }

                }
                if ( args.getOptionValues( "app.action" ).contains( "deleteById" ) ) {
                    if (!args.containsOption("app.id")) {
                        System.err.println("id is missing");
                        return;
                    }
                    try {
                        int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                        playerResource.deletePlayerById( id );
                        System.out.println( "successfully deleted" );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Not found");
                    }
                }

            }
            if ( args.getOptionValues( "app.entity" ).contains( "team" ) )
            {
                if ( args.getOptionValues( "app.action" ).contains( "createAndRead" ) ) {
                    if ( !args.containsOption("app.name") || !args.containsOption("app.playersIds")
                            || !args.containsOption("app.membersCount") )
                    {
                        System.err.println("Missing name or players or membersCount");
                        return;
                    }
                    String name = args.getOptionValues("app.name").get(0);
                    Integer membersCount = Integer.parseInt( args.getOptionValues("app.membersCount").get(0) );
                    List <Integer> playersIds = new ArrayList<>();
                    for ( String idStr : args.getOptionValues("app.playersIds") )
                    {
                        playersIds.add( Integer.parseInt( idStr ) );
                    }
                    TeamCreateDTO teamCreateDTO = new TeamCreateDTO( name, membersCount, playersIds);
                    try {
                        teamResource.create (teamCreateDTO);
                        System.out.println("successfully created");
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.CONFLICT)
                            System.err.println("Team with this name already exists");
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                            System.err.println("Some playersIds were not found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "update" ) ) {
                    if ( !args.containsOption("app.name") || !args.containsOption("app.playersIds")
                            || !args.containsOption("app.membersCount") || !args.containsOption("app.id"))
                    {
                        System.err.println("Missing name or playersIds or membersCount or id");
                        return;
                    }
                    String name = args.getOptionValues("app.name").get(0);
                    Integer membersCount = Integer.parseInt( args.getOptionValues("app.membersCount").get(0) );
                    List <Integer> playersIds = new ArrayList<>();
                    for ( String idStr : args.getOptionValues("app.playersIds") )
                    {
                        playersIds.add( Integer.parseInt( idStr ) );
                    }
                    int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                    TeamCreateDTO teamCreateDTO = new TeamCreateDTO(name, membersCount, playersIds);
                    try {
                        teamResource.update ( id ,teamCreateDTO);
                        System.out.println( "Successfully updated");
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                            System.err.println("Not Found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readById" ) ) {
                    if (!args.containsOption("app.id")) {
                        System.err.println("id is missing");
                        return;
                    }
                    try {
                        int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                        System.out.println( teamResource.readById(id).getName() );
                        System.out.println( "AmountOfPlayers: " + teamResource.readById(id).getMembersCount() );
                        System.out.println( "Linked playersIds:" );
                        System.out.println( teamResource.readById(id).getPlayersIds() );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Not found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readAll" ) ) {
                    try {
                        List<TeamDTO> teamsReceived;
                        teamsReceived = teamResource.readAll();

                        for (TeamDTO p : teamsReceived) {
                            System.out.println(p.getId());
                            System.out.println(p.getName());
                            System.out.println("AmountOfPlayers: " + p.getMembersCount());
                            System.out.println("Linked playersIds:");
                            System.out.println(p.getPlayersIds());
                            System.out.println("***********************************************");
                        }
                    }catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Could not find any teams");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "deleteAndMigrate" ) ) {
                    if (!args.containsOption("app.idDelete") || !args.containsOption("app.idMigrate") ) {
                        System.err.println("idDelete or idMigrate is missing");
                        return;
                    }
                    try {
                        int idDelete = Integer.parseInt(args.getOptionValues("app.idDelete").get(0));
                        int idMigrate = Integer.parseInt(args.getOptionValues("app.idMigrate").get(0));
                        teamResource.deleteTeamByIdAndMigratePlayers( idDelete, idMigrate );
                        System.out.println( "successfully deleted and migrated" );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Not found");
                    }
                }
            }

            if ( args.getOptionValues( "app.entity" ).contains( "club" ) )
            {
                if ( args.getOptionValues( "app.action" ).contains( "createAndRead" ) ) {
                    if ( !args.containsOption("app.name") || !args.containsOption("app.teamsIds"))
                    {
                        System.err.println("Missing name or teams");
                        return;
                    }
                    String name = args.getOptionValues("app.name").get(0);
                    List <Integer> teamsIds = new ArrayList<>();
                    for ( String idStr : args.getOptionValues("app.teamsIds") )
                    {
                        teamsIds.add( Integer.parseInt( idStr ) );
                    }
                    ClubCreateDTO clubCreateDTO = new ClubCreateDTO( name, teamsIds);
                    try {
                        clubResource.create (clubCreateDTO);
                        System.out.println( "successfully created" );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.CONFLICT)
                            System.err.println("Club with this name already exists");
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                            System.err.println("Some teams were not found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "update" ) ) {
                    if ( !args.containsOption("app.name") || !args.containsOption("app.teamsIds")
                            || !args.containsOption("app.id"))
                    {
                        System.err.println("Missing name or teamsIds or id");
                        return;
                    }
                    String name = args.getOptionValues("app.name").get(0);
                    List <Integer> teamsIds = new ArrayList<>();
                    for ( String idStr : args.getOptionValues("app.teamsIds") )
                    {
                        teamsIds.add( Integer.parseInt( idStr ) );
                    }
                    int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                    ClubCreateDTO clubCreateDTO = new ClubCreateDTO(name, teamsIds);
                    try {
                        clubResource.update ( id ,clubCreateDTO);
                        System.out.println( "Successfully updated");
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                            System.err.println("Not Found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readById" ) ) {
                    if (!args.containsOption("app.id")) {
                        System.err.println("id is missing");
                        return;
                    }
                    try {
                        int id = Integer.parseInt(args.getOptionValues("app.id").get(0));
                        System.out.println( clubResource.readById(id).getName());
                        System.out.println( "Linked teamsIds:" );
                        System.out.println( clubResource.readById(id).getTeamsIds() );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Not found");
                    }
                }
                if ( args.getOptionValues( "app.action" ).contains( "readAll" ) ) {
                    try {
                        List<ClubDTO> clubsReceived;
                        clubsReceived = clubResource.readAll();

                        for (ClubDTO p : clubsReceived) {
                            System.out.println(p.getId());
                            System.out.println(p.getName());
                            System.out.println("Linked teamsIds:");
                            System.out.println(p.getTeamsIds());
                            System.out.println("***********************************************");
                        }
                    }catch (HttpClientErrorException e) {
                        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                            System.err.println("Could not find any clubs");
                    }
                }
            }
        }
    }
}
