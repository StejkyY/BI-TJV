package cz.cvut.fit.stejsj27.semestralka_klient.dto;

import java.util.Objects;

public class PlayerCreateDTO {
    private final String firstName;
    private final String lastName;

    public PlayerCreateDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerCreateDTO)) return false;
        PlayerCreateDTO that = (PlayerCreateDTO) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
