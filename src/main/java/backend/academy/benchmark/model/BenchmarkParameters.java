package backend.academy.benchmark.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BenchmarkParameters {
    public static final int FORKS = 3;
    public static final int WARMUP_FORKS = 1;
    public static final int WARMUP_ITERATIONS = 10;
    public static final long WARMUP_TIME_SECONDS = 5;
    public static final int MEASUREMENT_ITERATIONS = 20;
    public static final long MEASUREMENT_TIME_SECONDS = 10;
}
