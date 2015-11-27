package ru.nojs.inject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.vdovin.inject.ContainerImp;
import ru.vdovin.inject.Eager;
import ru.vdovin.inject.ScanPackage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ContainerTest {
    static Container container = new ContainerImp("ru.nojs.inject");

    @Test
    public void testSimpleInstance() {
        SimpleInstance instance = container.getInstance(SimpleInstance.class);
        Assert.assertTrue("Responding!", instance.method());
    }

    @Test
    public void testSimpleInstances() {
        SimpleInstance instance1 = container.getInstance(SimpleInstance.class);
        SimpleInstance instance2 = container.getInstance(SimpleInstance.class);
        Assert.assertNotEquals("Got new instance every time", instance1, instance2);
    }

    @Test
    public void testSingletonInstance() {
        SimpleSingleton singleton1 = container.getInstance(SimpleSingleton.class);
        SimpleSingleton singleton2 = container.getInstance(SimpleSingleton.class);
        Assert.assertEquals("No double'tons allowed", singleton1, singleton2);
    }

    @Test
    public void testThreadedSingletonInstance() {
        CountDownLatch latch = new CountDownLatch(2);
        Callable<SimpleSingleton> s = () -> {
            latch.await();
            return container.getInstance(SimpleSingleton.class);
        };

        ExecutorService es = Executors.newFixedThreadPool(2);
        List<Future<SimpleSingleton>> fs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fs.add(es.submit(s));
            latch.countDown();
        }

        List<SimpleSingleton> ss = fs.stream().map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());


        SimpleSingleton singleton = ss.get(0);
        Assert.assertTrue(
                "No double'tons allowed",
                ss
                        .stream()
                        .peek(System.out::println)
                        .allMatch(newSingleton -> singleton == newSingleton));
    }

    @Test
    public void testByName() {
        NamedInstance ni = container.getInstance("namedBean", NamedInstance.class);
        Assert.assertTrue("I am binded by name", ni.method());
    }

    @Test
    public void testComplexInjection() {
        MoreComplexClass complexClass1 = container.getInstance(MoreComplexClass.class);
        Assert.assertTrue("Wired correctly", complexClass1.singleton().method());
        MoreComplexClass complexClass2 = container.getInstance(MoreComplexClass.class);
        Assert.assertNotEquals("Got new instance", complexClass1, complexClass2);
        Assert.assertEquals("But still the same singleton", complexClass1.singleton(), complexClass2.singleton());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAmbiguousInjection() {
        container.getInstance(AmbiguousComplexClass.class);
    }

    @Test // bonus level
    public void testInterfaceBinding() {
        MultipleImplementations impl1 = container.getInstance("first", MultipleImplementations.class);
        MultipleImplementations impl2 = container.getInstance("second", MultipleImplementations.class);
        Assert.assertFalse("This is first implementation", impl1.method());
        Assert.assertTrue("This is second implementation", impl2.method());
    }

    @Test // bonus level 2
    public void testReallyComplexInjection() {
        ComplexInjection complex = container.getInstance(ComplexInjection.class);
        Assert.assertFalse("This is first implementation", complex.a().method());
        Assert.assertTrue("This is second implementation", complex.b().method());
    }

    @Test(expected = IllegalStateException.class)
    public void testAmbiguousInstanceInjection() {
        container.getInstance(NotQualified.class);
    }

    @Ignore
    @Test(expected = IllegalStateException.class) // Bonus level 3
    public void testCircularDependencyCircuitBreak() throws Exception {
        CompletableFuture<CircularDependencyA> cf =
                CompletableFuture.supplyAsync(() -> container.getInstance(CircularDependencyA.class));
        cf.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testEagerSingletonInstance() {
        EagerSingleton singleton1 = container.getInstance(EagerSingleton.class);
        EagerSingleton singleton2 = container.getInstance(EagerSingleton.class);
        Assert.assertEquals("No double'tons allowed", singleton1, singleton2);
    }


    public static class SimpleInstance {
        boolean method() {
            return true;
        }
    }

    @Singleton
    public static class SimpleSingleton {
        public SimpleSingleton() {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) { }
        }
        boolean method() {
            return true;
        }
    }

    @Named("namedBean")
    public static class NamedInstance {
        boolean method() {
            return true;
        }
    }

    public static class MoreComplexClass {
        private final SimpleSingleton singleton;

        public MoreComplexClass(SimpleSingleton singleton) {
            this.singleton = singleton;
        }

        public SimpleSingleton singleton() {
            return singleton;
        }
    }

    public static class AnnotatedMoreComplexClass {
        private final SimpleSingleton singleton;

        public AnnotatedMoreComplexClass(SimpleSingleton singleton, String someVar) {
            this.singleton = null;
        }

        @Inject
        public AnnotatedMoreComplexClass(SimpleSingleton singleton) {
            this.singleton = singleton;
        }

        public SimpleSingleton singleton() {
            return singleton;
        }
    }


    public static class AmbiguousComplexClass {
        public AmbiguousComplexClass(SimpleSingleton singleton, SimpleSingleton singleton2) {

        }
        public AmbiguousComplexClass(MoreComplexClass clazz) {

        }
    }

    //@ScanPackage("ru.nojs.inject")
    public interface MultipleImplementations {
        boolean method();
    }

    @Named("first")
    public static class FirstImplementation implements MultipleImplementations {
        @Override
        public boolean method() {
            return false;
        }
    }

    @Named("second")
    public static class SecondImplementation implements MultipleImplementations {
        @Override
        public boolean method() {
            return true;
        }
    }

    @Singleton
    public static class ComplexInjection {
        private final MultipleImplementations impl1;
        private final MultipleImplementations impl2;

        @Inject
        public ComplexInjection(
                @Named("first") MultipleImplementations impl1,
                @Named("second") MultipleImplementations impl2) {
            this.impl1 = impl1;
            this.impl2 = impl2;
        }

        public MultipleImplementations a() {
            return impl1;
        }

        public MultipleImplementations b() {
            return impl2;
        }
    }

    @Singleton
    public static class NotQualified {
        @Inject
        public NotQualified(MultipleImplementations implementations) {

        }
    }

    @Singleton
    public static class CircularDependencyA {
        @Inject
        public CircularDependencyA(CircularDependencyB b) {}

    }

    @Singleton
    public static class CircularDependencyB {
        @Inject
        public CircularDependencyB(CircularDependencyA a) {

        }
    }

    @Eager
    @Singleton
    public static class EagerSingleton {
        boolean method() {
            return true;
        }
    }
}
