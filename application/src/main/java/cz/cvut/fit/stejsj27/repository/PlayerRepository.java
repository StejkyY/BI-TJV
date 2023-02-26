package cz.cvut.fit.stejsj27.repository;

import cz.cvut.fit.stejsj27.dto.PlayerDTO;
import cz.cvut.fit.stejsj27.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository <Player, Integer> {
    List <Player> findByLastName ( String lastName );
}
