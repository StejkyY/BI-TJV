package cz.cvut.fit.stejsj27.dto;

import java.util.List;
import java.util.Objects;

public class TeamCreateDTO {

    private final String name;
    private final int membersCount;
    private final List<Integer> playersIds;

    public TeamCreateDTO(String name, int membersCount,  List<Integer> playersIds) {
        this.name = name;
        this.membersCount = membersCount;
        this.playersIds = playersIds;
    }

    public String getName() {
        return name;
    }
    public List<Integer> getPlayersIds() {
        return playersIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamCreateDTO)) return false;
        TeamCreateDTO that = (TeamCreateDTO) o;
        return membersCount == that.membersCount &&
                name.equals(that.name) &&
                playersIds.equals(that.playersIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, membersCount, playersIds);
    }

    public int getMembersCount() {
        return membersCount;
    }
}
