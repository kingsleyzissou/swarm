package io.swarm.benchmarks;

import io.swarm.collections.DisjointSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;

@Measurement(iterations = 5)
@Warmup(iterations = 2)
@Fork(value=1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MyBenchmark {

    public static void main(String[] args) throws RunnerException, IOException {

        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
    public static class BenchmarkState {

        public DisjointSet set;

        @Setup (Level.Trial)
        public void initialise() throws IOException, ClassNotFoundException {
            set = load();
        }

        private DisjointSet load() throws IOException, ClassNotFoundException {
            FileInputStream fis = new FileInputStream("./resources/vset.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            return set =   (DisjointSet) ois.readObject();
        }

        public DisjointSet getSet() {
            return set;
        }

        public DisjointSet getMergedSet() {
            set.generateClusters();
            return set;
        }
    }

    @Benchmark
    public void clusterCreationBenchmark (BenchmarkState state) {
        state.set.generateClusters();
    }

    @Benchmark
    public void countBirds (BenchmarkState state) {
        DisjointSet set = state.getMergedSet();
//        set.countSets();
    }

}
