package ru.nojs.inject;

import org.junit.Assert;
import org.junit.Test;
import ru.vdovin.inject.Digraph;
import ru.vdovin.inject.Eager;
import ru.vdovin.inject.ContainerImp;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ContainerTest {
    static Container container = new ContainerImp("ru.nojs.inject");

    @Test
    public void testGraph() {

        Digraph<Integer> dg = new Digraph<>();
        dg.add(1,2);
        dg.add(1,3);
        dg.add(1,4);
        dg.add(4, 5);
        Assert.assertTrue("Circular", dg.isCircular(5, 1));

        dg.add(2, 5);
        Assert.assertTrue("Circular", dg.isCircular(5, 1));

        dg.add(6);
        Assert.assertFalse("Not Circular", dg.isCircular(5, 6));
    }


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

    @Test(timeout = 1500) // Bonus level 3
    public void testCircularDependencyCircuitBreak() throws Exception {
        CompletableFuture<CircularDependencyA> cf =
                CompletableFuture.supplyAsync(() -> container.getInstance(CircularDependencyA.class));
        cf.handle(
                (instance, th) -> {
                    Assert.assertNull("Should be empty", instance);
                    Assert.assertTrue("Cause of exception is IAE", th.getCause() instanceof IllegalStateException);
                    return null;
                }).get(1, TimeUnit.SECONDS);
    }

    @Test(timeout = 1500)
    public void testNotCircularDependencyEasy() throws Exception {
        RootClass rc = container.getInstance(RootClass.class);
        Assert.assertNotNull("It's not a circular dependency", rc);
    }


    @Test(timeout = 1500)
    public void testNotCircularDependency() throws Exception {
        CompletableFuture.supplyAsync(() -> container.getInstance(RootClass.class))
                .handle(
                        (rc, t) -> {
                            Assert.assertNotNull("Instance created", rc);
                            Assert.assertNull("Exception not thrown", t);
                            return null;
                        }
                ).get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testEagerSingletonInstance() throws Exception {
        Map<Class, Object> eagerSingleton = ((ContainerImp)container).getAllSingletonObj();
        Assert.assertTrue("Eager instance ok", eagerSingleton.containsKey(EagerSingleton.class));
        Assert.assertFalse("No Eager  - no instance", eagerSingleton.containsKey(LazySingleton.class));
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

    @Singleton
    public static class RootClass {
        @Inject
        public RootClass(NotSoCircularDependencyA a, NotSoCircularDependencyB b) {

        }
    }

    @Singleton
    public static class NotSoCircularDependencyA {
        @Inject
        public NotSoCircularDependencyA(@Named("first") MultipleImplementations first) {

        }
    }

    @Singleton
    public static class NotSoCircularDependencyB {
        @Inject
        public NotSoCircularDependencyB(@Named("first") MultipleImplementations first) {

        }
    }

    @Singleton
    public static class NotSoCircularDependencyE {
        @Inject
        public NotSoCircularDependencyE(SimpleSingleton singleton) {

        }
    }

    @Singleton
    public static class NotSoCircularDependencyD {
        @Inject
        public NotSoCircularDependencyD(SimpleSingleton singleton) {

        }
    }

    @Eager
    @Singleton
    public static class EagerSingleton {
        boolean method() {
            return true;
        }
    }

    @Singleton
    public static class LazySingleton {
        boolean method() {
            return true;
        }
    }

}
