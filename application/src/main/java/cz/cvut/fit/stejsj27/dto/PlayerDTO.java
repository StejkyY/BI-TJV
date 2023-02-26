package cz.cvut.fit.stejsj27.dto;

import com.sun.istack.NotNull;
import cz.cvut.fit.stejsj27.entity.Player;
import cz.cvut.fit.stejsj27.entity.Team;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

public class PlayerDTO {

    private final int id;
    private final String firstName;
    private final String lastName;

    public PlayerDTO(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerDTO)) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return id == playerDTO.id &&
                firstName.equals(playerDTO.firstName) &&
                lastName.equals(playerDTO.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
