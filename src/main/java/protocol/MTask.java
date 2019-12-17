package protocol;

abstract public class MTask extends MMessage {

    public interface Executable {
        void execute(int id, String content);
    }

    abstract public void handle(Executable e);
}
