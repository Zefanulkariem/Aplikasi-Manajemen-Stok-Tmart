package base;

public abstract class BaseEntity {

    protected String id;

    public BaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract boolean validate();

    public abstract String toString();
}
