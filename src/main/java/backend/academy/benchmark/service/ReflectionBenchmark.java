package backend.academy.benchmark.service;

import backend.academy.benchmark.model.Student;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Measurement(iterations = 5, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
public class ReflectionBenchmark {

    private Student student;
    private Method method;
    private MethodHandles.Lookup lookup;
    private java.util.function.Function<Student, String> lambda;

    @Setup
    public void setup() throws Throwable {
        student = new Student("Alexander", "Biryukov");

        // Reflection setup
        method = Student.class.getMethod("name");

        // MethodHandles setup
        lookup = MethodHandles.lookup();

        // LambdaMetafactory setup
        var methodHandle = lookup.findVirtual(Student.class, "name", MethodType.methodType(String.class));
        lambda = (java.util.function.Function<Student, String>) LambdaMetafactory.metafactory(
                lookup,
                "apply",
                MethodType.methodType(java.util.function.Function.class),
                methodHandle.type().generic(),
                methodHandle,
                methodHandle.type()
        ).getTarget().invokeExact();
    }

    @Benchmark
    public void directAccess(Blackhole bh) {
        bh.consume(student.name());
    }

    @Benchmark
    public void reflection(Blackhole bh) throws Exception {
        bh.consume(method.invoke(student));
    }

    @Benchmark
    public void methodHandles(Blackhole bh) throws Throwable {
        var handle = lookup.findVirtual(Student.class, "name", MethodType.methodType(String.class));
        bh.consume((String) handle.invokeExact(student));
    }

    @Benchmark
    public void lambdaMetafactory(Blackhole bh) {
        bh.consume(lambda.apply(student));
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ReflectionBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .build();

        new Runner(options).run();
    }
}
