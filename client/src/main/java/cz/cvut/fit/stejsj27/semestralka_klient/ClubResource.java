package cz.cvut.fit.stejsj27.semestralka_klient;

import cz.cvut.fit.stejsj27.semestralka_klient.dto.ClubCreateDTO;
import cz.cvut.fit.stejsj27.semestralka_klient.dto.ClubDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class ClubResource {

    private final RestTemplate restTemplate;

    public ClubResource(RestTemplateBuilder restTemplateBuilder )
    {
        restTemplate = restTemplateBuilder.rootUri( ROOT_RESOURCE_URL ).build();
    }

    private static final String ROOT_RESOURCE_URL = "http://localhost:8080/club";
    private static final String ONE_URI = "/{id}";

    public URI create (ClubCreateDTO clubCreateDTO)
    {
        return restTemplate.postForLocation("/", clubCreateDTO);
    }

    public ClubDTO readById ( int id )
    {
        return restTemplate.getForObject(ONE_URI,
                ClubDTO.class, id );
    }

    public List<ClubDTO> readAll ( )
    {
        ResponseEntity < List < ClubDTO > > response = restTemplate.exchange
                ( "/readAll", HttpMethod.GET, null, new ParameterizedTypeReference< List < ClubDTO > >() {} );
        return response.getBody();
    }

    public void update ( int id, ClubCreateDTO clubCreateDTO )
    {
        restTemplate.put( ONE_URI, clubCreateDTO, id );
    }


}
