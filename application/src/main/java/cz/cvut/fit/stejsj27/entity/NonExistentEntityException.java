package cz.cvut.fit.stejsj27.entity;

public class NonExistentEntityException extends IllegalArgumentException {
    public NonExistentEntityException() {
        super("Entity or sub entity not found");
    }
}
