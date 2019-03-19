package io.swarm.benchmarks;

import io.swarm.collections.DisjointImage;
import io.swarm.collections.DisjointSet;
import io.swarm.stats.FlockRegression;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Measurement(iterations = 5)
@Warmup(iterations = 2)
@Fork(value=1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class DisjointImageBenchmarkTest {

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(DisjointImageBenchmarkTest.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {

        public DisjointSet set;
        public DisjointImage image;
        public DisjointImage filtered;

        @Setup (Level.Trial)
        public void initialise() throws IOException {
            Image img = test();
            set = new DisjointSet(img.getWidth(), img.getHeight());
            image = new DisjointImage(img, set, null);
            filtered = new DisjointImage(img, set, null);
            filtered.filter();
        }

        private Image test() throws IOException {
            BufferedImage img = ImageIO.read(new File("./src/main/resources/assets/Birds.jpg"));
            return SwingFXUtils.toFXImage(img, null);
        }

    }

    @Benchmark
    public void a_filterImageBenchmark(BenchmarkState state) {
        state.image.filter();
    }

    @Benchmark
    public void b_clusterCreationBenchmark (BenchmarkState state) {
        state.filtered.getSet().generateClusters();
    }

    @Benchmark
    public void c_countBirds (BenchmarkState state) {
        state.filtered.getSet().generateClusters();
        state.filtered.getSet().countSets();
    }

    @Benchmark
    public void d_analyseFormation(BenchmarkState state) {
        new FlockRegression(state.filtered.getSet(), state.filtered.getSet().getWidth());
    }

    @Benchmark
    public void e_serializeCluster(BenchmarkState state) {
        state.filtered.getSet().getSerializedClusterSizes();
    }

}
