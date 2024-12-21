package backend.academy.benchmark.service;

import backend.academy.benchmark.model.Student;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ReflectionBenchmark {
    private Student student;
    private Method method;
    private MethodHandle methodHandle;
    private java.util.function.Function<Student, String> lambda;

    private static final String TEST_NAME = "Mishel";
    private static final String TEST_SURNAME = "Ganin";
    private static final String METHOD_NAME = "name";
    private static final String INTERACTIVE_METHOD_NAME = "apply";

    @Setup
    public void setup() throws Throwable {
        student = new Student(TEST_NAME, TEST_SURNAME);
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        method = Student.class.getMethod(METHOD_NAME);
        methodHandle = lookup.findVirtual(Student.class, METHOD_NAME, MethodType.methodType(String.class));
        lambda = getLambda(lookup, methodHandle);
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
        bh.consume((String) methodHandle.invokeExact(student));
    }

    @Benchmark
    public void lambdaMetafactory(Blackhole bh) {
        bh.consume(lambda.apply(student));
    }

    private java.util.function.Function<Student, String> getLambda(
            MethodHandles.Lookup lookup, MethodHandle methodHandle) throws Throwable {
        return (java.util.function.Function<Student, String>) LambdaMetafactory.metafactory(
                lookup,
                INTERACTIVE_METHOD_NAME,
                MethodType.methodType(java.util.function.Function.class),
                methodHandle.type().generic(),
                methodHandle,
                methodHandle.type()
        ).getTarget().invokeExact();
    }
}
