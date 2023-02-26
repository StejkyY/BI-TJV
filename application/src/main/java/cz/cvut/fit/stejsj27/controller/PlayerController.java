package cz.cvut.fit.stejsj27.controller;

import cz.cvut.fit.stejsj27.dto.PlayerCreateDTO;
import cz.cvut.fit.stejsj27.dto.PlayerDTO;
import cz.cvut.fit.stejsj27.dto.TeamDTO;
import cz.cvut.fit.stejsj27.entity.NonExistentEntityException;
import cz.cvut.fit.stejsj27.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/player/readAll")
    List<PlayerDTO> readAll() {
        try {
            return playerService.findAll();
        } catch (NonExistentEntityException e )
        {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/player/{id}")
    PlayerDTO readById( @PathVariable int id) {
        return playerService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException (HttpStatus.NOT_FOUND));
    }

    @GetMapping( value = "/player/", params = { "playerLastName" } )
    List < PlayerDTO > readByPlayerLastName ( @RequestParam String playerLastName ) {
        return playerService.findByLastName(playerLastName);
    }

    @PostMapping("/player")
    PlayerDTO create(@RequestBody PlayerCreateDTO player) {
       return playerService.create(player);
    }

    @PutMapping("/player/{id}")
    PlayerDTO update( @PathVariable int id, @RequestBody PlayerCreateDTO player) {
        try { return playerService.update(id, player); }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }

    }

    @DeleteMapping("/player/{id}")
    void deletePlayerById ( @PathVariable int id )
    {
        try { playerService.deleteById(id); }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }
}
