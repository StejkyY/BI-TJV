package cz.cvut.fit.stejsj27.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Club {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String name;

    @OneToMany
    @JoinColumn( name = "club" )
    private List<Team> teams;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Club)) return false;
        Club club = (Club) o;
        return id == club.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Club() {
    }

    public Club(String name, List<Team> teams) {
        this.name = name;
        this.teams = teams;
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
