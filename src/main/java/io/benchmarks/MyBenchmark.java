package io.benchmarks;

import io.helpers.ImageHandler;
import io.helpers.SetHandler;
import io.swarm.DisjointSet;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Measurement(iterations = 5)
@Warmup(iterations = 2)
@Fork(value=1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MyBenchmark {

    public static void main(String[] args) throws RunnerException, IOException {

        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class ImageWrapper {

        public DisjointSet set;
        public Image original;
        public WritableImage grayScale;
        public double height;
        public double width;

        public ImageWrapper() {
            File file = new File("./resources/assets/b.png");
            original = new Image(file.toURI().toString());
            height = original.getHeight();
            width = original.getWidth();
            set = new DisjointSet((int) width,(int) height);
        }
    }

    @Benchmark
    public void testMethod(ImageWrapper image) {
        ImageHandler.filter(image.original, image.grayScale, image.set);
        SetHandler.createSets(image.set, (int) image.width);
    }

}
