package cz.cvut.fit.stejsj27.controller;

import cz.cvut.fit.stejsj27.dto.TeamCreateDTO;
import cz.cvut.fit.stejsj27.dto.TeamDTO;
import cz.cvut.fit.stejsj27.entity.ExistingEntityException;
import cz.cvut.fit.stejsj27.entity.NonExistentEntityException;
import cz.cvut.fit.stejsj27.entity.Team;
import cz.cvut.fit.stejsj27.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/team/readAll")
    List<TeamDTO> readAll() {
        try {
            return teamService.findAll();
        }catch (NonExistentEntityException e )
        {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/team/{id}")
    TeamDTO readById( @PathVariable int id) {
        return teamService.findByIdAsDTO(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping( value = "/team", params = {"teamName"})
    TeamDTO readByTeamName ( @RequestParam String teamName ) {
        return teamService.findByName(teamName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping( value = "/team", params = {"membersCount"})
    List<TeamDTO> readByMembersCount(@RequestParam int membersCount) {
        return teamService.findAllTeamsByMembersCount ( membersCount );
    }

    @GetMapping( value = "/team", params = {"lastName"})
    List<TeamDTO> readByPlayerLastName(@RequestParam String lastName) {
        return teamService.findAllTeamsByPlayerLastName(lastName);
    }

    @PostMapping("/team")
    TeamDTO create( @RequestBody TeamCreateDTO team) {
        try { return teamService.create(team); }
        catch ( ExistingEntityException m ) {
            throw new ResponseStatusException( HttpStatus.CONFLICT );
        }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

    @PutMapping("/team/{id}")
    TeamDTO update( @PathVariable int id, @RequestBody TeamCreateDTO team) {
        try { return teamService.update(id, team); }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

    @DeleteMapping("/team/{id_delete}/{id_migrate}")
    TeamDTO deleteTeamByIdAndMigratePlayers ( @PathVariable int id_delete, @PathVariable int id_migrate ) {
        try { return teamService.deleteAndMigratePlayersToDifferentTeam( id_delete, id_migrate ); }
        catch ( NonExistentEntityException e ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }
    }

}
