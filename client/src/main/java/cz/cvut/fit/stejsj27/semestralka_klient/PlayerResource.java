package cz.cvut.fit.stejsj27.semestralka_klient;

import cz.cvut.fit.stejsj27.semestralka_klient.dto.PlayerCreateDTO;
import cz.cvut.fit.stejsj27.semestralka_klient.dto.PlayerDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class PlayerResource {

    private final RestTemplate restTemplate;

    private static final String ROOT_RESOURCE_URL = "http://localhost:8080/player";
    private static final String ONE_URI = "/{id}";

    public PlayerResource ( RestTemplateBuilder restTemplateBuilder )
    {
        restTemplate = restTemplateBuilder.rootUri( ROOT_RESOURCE_URL ).build();
    }

    public URI create (PlayerCreateDTO playerCreateDTO)
    {
        return restTemplate.postForLocation("/", playerCreateDTO);
    }

    public PlayerDTO readById ( int id )
    {
        return restTemplate.getForObject(ONE_URI,
                PlayerDTO.class, id );
    }

    public List <PlayerDTO> readByPlayerLastName ( String playerLastName )
    {
        ResponseEntity < List < PlayerDTO > > response = restTemplate.exchange
                ( "/?playerLastName={playerLastName}", HttpMethod.GET, null, new ParameterizedTypeReference< List < PlayerDTO > >() {},
                    playerLastName );
        return response.getBody();

    }

    public List<PlayerDTO> readAll ( )
    {
        ResponseEntity < List < PlayerDTO > > response = restTemplate.exchange
                ( "/readAll", HttpMethod.GET, null, new ParameterizedTypeReference< List < PlayerDTO > >() {} );
        return response.getBody();
    }

    public void update ( int id, PlayerCreateDTO playerCreateDTO )
    {
        restTemplate.put( ONE_URI, playerCreateDTO, id );
    }

    public void deletePlayerById ( int id )
    {
        restTemplate.delete( ONE_URI, id );
    }

}
