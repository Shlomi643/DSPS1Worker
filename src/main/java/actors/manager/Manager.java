package actors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.Utils.*;

public class Manager {

    private static final int MAX_WORKERS = 20;
    private ExecutorService workerPool;
    private ExecutorService localsPool;

    public Manager() {
        this.workerPool = Executors.newCachedThreadPool();
        this.localsPool = Executors.newCachedThreadPool();

    }

    private void terminate() {
//        ExecutorService
    }

    public static void main(String[] args) {

    }
}
