package client;

import java.util.ArrayDeque;

public class LoopManager {
    private ArrayDeque<Executor> executorQueue;

    public LoopManager() {
        this.executorQueue = new ArrayDeque<>();
    }

    public void execute(Executor executor) {
        this.executorQueue.addFirst(executor);
    }

    public void loop() {
        while (!this.executorQueue.isEmpty())
            this.executorQueue.removeLast().execute();
    }

    public interface Executor {
        void execute();
    }
}
