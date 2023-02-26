package cz.cvut.fit.stejsj27.semestralka_klient.dto;

import java.util.List;
import java.util.Objects;

public class TeamDTO {

    private final int id;
    private final String name;
    private final int membersCount;
    private final List<Integer> playersIds;

    public TeamDTO(int id, String name, int membersCount, List<Integer> playersIds) {
        this.id = id;
        this.name = name;
        this.membersCount = membersCount;
        this.playersIds = playersIds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getPlayersIds() {
        return playersIds;
    }

    public int getMembersCount() {
        return membersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamDTO)) return false;
        TeamDTO teamDTO = (TeamDTO) o;
        return id == teamDTO.id &&
                membersCount == teamDTO.membersCount &&
                name.equals(teamDTO.name) &&
                playersIds.equals(teamDTO.playersIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, membersCount, playersIds);
    }
}
