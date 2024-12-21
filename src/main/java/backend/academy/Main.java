package backend.academy;

import backend.academy.benchmark.service.ReflectionBenchmark;
import lombok.experimental.UtilityClass;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@UtilityClass
public class Main {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ReflectionBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .forks(1)
                .warmupForks(1)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(5))
                .measurementIterations(10)
                .measurementTime(TimeValue.seconds(5))
                .build();

        new Runner(options).run();
    }
}
