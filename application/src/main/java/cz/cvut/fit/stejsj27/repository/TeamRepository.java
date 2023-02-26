package cz.cvut.fit.stejsj27.repository;

import cz.cvut.fit.stejsj27.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    Optional<Team> findByName ( String name );

    @Query(
            "SELECT teamWithMembersCount FROM Team teamWithMembersCount " +
                    "JOIN teamWithMembersCount.players players " +
                    "WHERE players.size = :membersCount"
    )
    List<Team> findAllTeamsByMembersCount ( int membersCount );

    @Query(
            "SELECT anotherTeamOfPlayer FROM Team anotherTeamOfPlayer " +
                    "JOIN anotherTeamOfPlayer.players players " +
                    "WHERE lower ( players.lastName ) = lower(:lastName) "
    )
    List<Team> findAllTeamsByPlayerLastName (String lastName );
}
