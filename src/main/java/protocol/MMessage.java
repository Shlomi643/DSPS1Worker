package protocol;


public abstract class MMessage {

    protected String id;

    @Override
    abstract public String toString();


    public String getId() {
        return id;
    }
}
