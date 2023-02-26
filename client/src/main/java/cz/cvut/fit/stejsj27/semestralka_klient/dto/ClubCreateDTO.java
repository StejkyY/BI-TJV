package cz.cvut.fit.stejsj27.semestralka_klient.dto;

import java.util.List;
import java.util.Objects;

public class ClubCreateDTO {
    private final String name;
    private final List<Integer> teamsIds;

    public ClubCreateDTO(String name, List<Integer> teamsIds) {
        this.name = name;
        this.teamsIds = teamsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClubCreateDTO)) return false;
        ClubCreateDTO that = (ClubCreateDTO) o;
        return name.equals(that.name) &&
                teamsIds.equals(that.teamsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teamsIds);
    }

    public String getName() {
        return name;
    }

    public List<Integer> getTeamsIds() {
        return teamsIds;
    }
}
