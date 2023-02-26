package cz.cvut.fit.stejsj27.dto;

import java.util.List;
import java.util.Objects;

public class ClubDTO {

    private final int id;
    private final String name;
    private final List<Integer> teamsIds;

    public ClubDTO(int id, String name, List<Integer> teamsIds) {
        this.id = id;
        this.name = name;
        this.teamsIds = teamsIds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getTeamsIds() {
        return teamsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClubDTO)) return false;
        ClubDTO clubDTO = (ClubDTO) o;
        return id == clubDTO.id &&
                name.equals(clubDTO.name) &&
                teamsIds.equals(clubDTO.teamsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teamsIds);
    }
}
