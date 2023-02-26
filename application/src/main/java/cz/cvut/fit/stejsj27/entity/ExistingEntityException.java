package cz.cvut.fit.stejsj27.entity;

public class ExistingEntityException extends IllegalArgumentException {
    public ExistingEntityException() {
        super("Entity already existing");
    }
}
