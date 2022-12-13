package utility;

import utility.Action;

import java.util.function.Consumer;

public class CallBackRunnable<T> implements Runnable {
    private Action action;
    private Consumer<T> consumer;
    private T parameter;

    public CallBackRunnable(Action action) {
        this.action = action;
    }

    public CallBackRunnable(Consumer<T> consumer, T parameter) {
        this.consumer = consumer;
        this.parameter = parameter;
    }

    @Override
    public void run() {
        if (action != null) {
            action.callBack();
        }
        if (consumer != null) {
            consumer.accept(parameter);
        }

    }
}
