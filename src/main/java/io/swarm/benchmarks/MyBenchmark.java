package io.swarm.benchmarks;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import javafx.scene.image.Image;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

        public BufferedImage original;
        public DisjointSet set;
        public DisjointImage image;
        public DisjointImage filtered;

        @Setup (Level.Trial)
        public void initialise() throws IOException {
            original = load();
            set = new DisjointSet(original.getWidth(), original.getHeight());
            image = new DisjointImage(original, set, null);
            filtered = new DisjointImage(original, set, null);
            filtered.filter();
        }

        private BufferedImage load() throws IOException {
           return ImageIO.read(new File("./src/main/resources/assets/Birds.jpg"));
        }

    }

    @Benchmark
    public void clusterCreationBenchmark (BenchmarkState state) {
        state.image.filter();
    }

//    @Benchmark
//    public void countBirds (BenchmarkState state) {
//        state.filtered.getSet().generateClusters();
//        state.filtered.getSet().countSets();
//    }

}
