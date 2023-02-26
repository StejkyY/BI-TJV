package cz.cvut.fit.stejsj27.repository;

import cz.cvut.fit.stejsj27.entity.Club;
import cz.cvut.fit.stejsj27.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {
    Optional<Club> findByName ( String name );
}
