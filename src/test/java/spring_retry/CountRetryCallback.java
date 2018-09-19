package spring_retry;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;

public class CountRetryCallback<T, E extends Throwable> implements RetryCallback<T, E> {

    private final RetryCallback<T, E> delegate;
    private int count;

    public CountRetryCallback(RetryCallback<T, E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T doWithRetry(RetryContext context) throws E {
        count++;
        return delegate.doWithRetry(context);
    }

    int getCount() {
        return count;
    }
}
