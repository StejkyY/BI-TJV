package cz.cvut.fit.stejsj27.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String name;

    @NotNull
    private int membersCount;

    @ManyToMany
    @JoinTable (
            name = "teams_and_players",
            joinColumns = @JoinColumn ( name = "team_id" ),
            inverseJoinColumns = @JoinColumn ( name = "player_id" )
    )
    private List<Player> players;

    public Team() {
    }

    public Team(String name, int membersCount, List<Player> players) {
        this.name = name;
        this.membersCount = membersCount;
        this.players = players;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return id == team.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
