package spring_retry;

import org.junit.jupiter.api.Test;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * 이 테스트의 목적은 Spring Retry 학습 입니다.
 * Spring Retry에 대한 내용 공식문서 혹은 아래 링크에 있는 Spring Retry.pdf 파일을 참고하길 바랍니다.
 * http://wiki.11stcorp.com/display/DevInno/Dev+Innovation+Seminar+-+2018
 */
public class RetryTemplateStudyTest {

    @Test
    void 콜백메소드_성공시_재시도_하지않음() {
        int maxAttempts = 3;

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy(maxAttempts);
        template.setRetryPolicy(policy);

        assertRetryExpectedTimes(template, 1, context -> 1);
    }

    @Test
    void 콜백메소드_실패시_특정횟수만큼_재시도() {
        int maxAttempts = 3;

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy(maxAttempts);
        template.setRetryPolicy(policy);

        Throwable t = catchThrowable(() -> assertRetryExpectedTimes(template,
                maxAttempts,
                context -> { throw new IllegalStateException(); }));
        assertThat(t).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 특정예외인_경우에만_재시도() {
        int maxAttempts = 3;

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy(maxAttempts, Collections.singletonMap(IOException.class, true));
        template.setRetryPolicy(policy);

        Throwable t = catchThrowable(() -> assertRetryExpectedTimes(template,
                1,
                context -> { throw new IllegalStateException(); }));
        assertThat(t).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 재시도_전부실패시_RecoveryCallback_호출() {
        int maxAttempts = 3;
        int defaultValue = 1;

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy(maxAttempts);
        template.setRetryPolicy(policy);

        int result = assertRetryExpectedTimes(template,
                maxAttempts,
                context -> { throw new IllegalStateException(); },
                context -> defaultValue);
        assertThat(result).isEqualTo(defaultValue);
    }

    @Test
    void 재시도시_고정된_기간만큼_BackOff() {
        int maxAttempts = 3;
        int backOffPeriod = 1000;

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy(maxAttempts);
        template.setRetryPolicy(policy);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backOffPeriod);
        template.setBackOffPolicy(backOffPolicy);

        Instant start = Instant.now();
        Throwable t = catchThrowable(() -> assertRetryExpectedTimes(template,
                maxAttempts,
                context -> { throw new IllegalStateException(); }));
        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toMillis();

        assertThat(timeElapsed).isGreaterThan((maxAttempts - 1) * backOffPeriod);
        assertThat(t).isInstanceOf(IllegalStateException.class);
    }

    private <T, E extends RuntimeException> T assertRetryExpectedTimes(RetryTemplate template,
                                                                       int expectedRetryCount,
                                                                       RetryCallback<T, E> callback) {
        return this.assertRetryExpectedTimes(template, expectedRetryCount, callback, null);
    }

    private <T, E extends RuntimeException> T assertRetryExpectedTimes(RetryTemplate template,
                                                                       int expectedRetryCount,
                                                                       RetryCallback<T, E> callback,
                                                                       RecoveryCallback<T> recoveryCallback) {
        CountRetryCallback<T, E> countRetryCallback = new CountRetryCallback<>(callback);
        T result = template.execute(countRetryCallback, recoveryCallback);
        assertThat(countRetryCallback.getCount()).isEqualTo(expectedRetryCount);
        return result;
    }
}
