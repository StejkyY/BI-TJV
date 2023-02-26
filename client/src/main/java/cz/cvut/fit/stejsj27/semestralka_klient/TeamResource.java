package cz.cvut.fit.stejsj27.semestralka_klient;

import cz.cvut.fit.stejsj27.semestralka_klient.dto.TeamCreateDTO;
import cz.cvut.fit.stejsj27.semestralka_klient.dto.TeamDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class TeamResource {

    private final RestTemplate restTemplate;

    public TeamResource(RestTemplateBuilder restTemplateBuilder )
    {
        restTemplate = restTemplateBuilder.rootUri( ROOT_RESOURCE_URL ).build();
    }

    private static final String ROOT_RESOURCE_URL = "http://localhost:8080/team";
    private static final String ONE_URI = "/{id}";

    public URI create (TeamCreateDTO teamCreateDTO)
    {
        return restTemplate.postForLocation("/", teamCreateDTO);
    }

    public TeamDTO readById ( int id )
    {
        return restTemplate.getForObject(ONE_URI,
                TeamDTO.class, id );
    }

    public List<TeamDTO> readAll ( )
    {
        ResponseEntity < List < TeamDTO > > response = restTemplate.exchange
                ( "/readAll", HttpMethod.GET, null, new ParameterizedTypeReference< List < TeamDTO > >() {} );
        return response.getBody();
    }

    public void update ( int id, TeamCreateDTO teamCreateDTO )
    {
        restTemplate.put( ONE_URI, teamCreateDTO, id );
    }

    public void deleteTeamByIdAndMigratePlayers ( int id_delete, int id_migrate )
    {
        restTemplate.delete( "/{id_delete}/{id_migrate}", id_delete, id_migrate );
    }

}
