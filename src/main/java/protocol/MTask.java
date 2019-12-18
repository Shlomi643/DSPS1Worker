package protocol;

abstract public class MTask extends MMessage {

    public interface Executable {
        void execute(String id, String content);
    }

    abstract public void handle(Executable e);
}
