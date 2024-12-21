package backend.academy;

import backend.academy.benchmark.service.ReflectionBenchmark;
import lombok.experimental.UtilityClass;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import static backend.academy.benchmark.model.BenchmarkParameters.FORKS;
import static backend.academy.benchmark.model.BenchmarkParameters.MEASUREMENT_ITERATIONS;
import static backend.academy.benchmark.model.BenchmarkParameters.MEASUREMENT_TIME_SECONDS;
import static backend.academy.benchmark.model.BenchmarkParameters.WARMUP_FORKS;
import static backend.academy.benchmark.model.BenchmarkParameters.WARMUP_ITERATIONS;
import static backend.academy.benchmark.model.BenchmarkParameters.WARMUP_TIME_SECONDS;

@UtilityClass
public class Main {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ReflectionBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .forks(FORKS)
                .warmupForks(WARMUP_FORKS)
                .warmupIterations(WARMUP_ITERATIONS)
                .warmupTime(TimeValue.seconds(WARMUP_TIME_SECONDS))
                .measurementIterations(MEASUREMENT_ITERATIONS)
                .measurementTime(TimeValue.seconds(MEASUREMENT_TIME_SECONDS))
                .build();

        new Runner(options).run();
    }
}
