package com.chibik.perf.asm.method;

import com.chibik.perf.BenchmarkRunner;
import com.chibik.perf.util.PrintAssembly;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

@State(Scope.Benchmark)
@PrintAssembly(printMethod = "*MethodHandleVsReflection.methodHandle")
public class MethodHandleVsReflection {

    private Method reflectionMethod;
    private MethodHandle methodHandle;
    private TestClass testInstance;

    @Setup
    public void setup() throws Exception {
        testInstance = new TestClass();
        
        reflectionMethod = TestClass.class.getDeclaredMethod("testMethod", int.class, int.class);
        reflectionMethod.setAccessible(true);
        
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        methodHandle = lookup.findVirtual(
            TestClass.class,
            "testMethod",
            MethodType.methodType(int.class, int.class, int.class)
        );
    }

    @Benchmark
    public void raw(Blackhole blackhole) {
        blackhole.consume(testInstance.testMethod(42, 65));
    }

    @Benchmark
    public void reflection(Blackhole blackhole) throws Exception {
        int result = (int) reflectionMethod.invoke(testInstance, 42, 65);
        blackhole.consume(result);
    }

    @Benchmark
    public void methodHandle(Blackhole blackhole) throws Throwable {
        int result = (int) methodHandle.invokeExact(testInstance, 42, 67);
        blackhole.consume(result);
    }

    public static class TestClass {
        @CompilerControl(CompilerControl.Mode.DONT_INLINE)
        public int testMethod(int x, int y) {
            return x * 2 + y;
        }
    }

    public static void main(String[] args) {
        BenchmarkRunner.run(MethodHandleVsReflection.class);
    }
}
