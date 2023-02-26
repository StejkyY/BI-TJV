package cz.cvut.fit.stejsj27.controller;

import cz.cvut.fit.stejsj27.dto.ClubCreateDTO;
import cz.cvut.fit.stejsj27.dto.ClubDTO;
import cz.cvut.fit.stejsj27.dto.PlayerDTO;
import cz.cvut.fit.stejsj27.entity.ExistingEntityException;
import cz.cvut.fit.stejsj27.entity.NonExistentEntityException;
import cz.cvut.fit.stejsj27.service.ClubService;
import cz.cvut.fit.stejsj27.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ClubController {
    
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/club/readAll")
    List<ClubDTO> readAll() {
        try { return clubService.findAll(); }
        catch (NonExistentEntityException e )
        {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/club/{id}")
    ClubDTO readById( @PathVariable int id) {
        return clubService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(name = "/club", params = {"clubName"})
    ClubDTO readByName ( @RequestParam String clubName ) {
        return clubService.findByName(clubName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/club")
    ClubDTO create( @RequestBody ClubCreateDTO club) {
        try { return clubService.create(club); }
        catch ( ExistingEntityException m ) {
            throw new ResponseStatusException( HttpStatus.CONFLICT );
        }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

    @PutMapping("/club/{id}")
    ClubDTO update( @PathVariable int id, @RequestBody ClubCreateDTO club) {
        try { return clubService.update(id, club); }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }
}
