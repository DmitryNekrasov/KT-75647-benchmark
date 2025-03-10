package singlesequenceof

import org.example.singlesequenceof.singleSequenceOf
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(2)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 5, time = 1)
open class SequenceBenchmarks {

    @Param("1000")
    private var count: Int = 0

    @Param("1", "100")
    private var operations: Int = 0

    // Basic creation benchmarks
    @Benchmark
    fun defaultSequenceOfSingleCreation(blackhole: Blackhole) {
        repeat(count) {
            val seq = sequenceOf(1)
            blackhole.consume(seq)
        }
    }

    @Benchmark
    fun singleSequenceOfCreation(blackhole: Blackhole) {
        repeat(count) {
            val seq = singleSequenceOf(1)
            blackhole.consume(seq)
        }
    }

    // Terminal operation benchmarks - first()
    @Benchmark
    fun defaultSequenceOfSingleFirst(blackhole: Blackhole) {
        repeat(count) {
            val result = sequenceOf(1).first()
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun singleSequenceOfFirst(blackhole: Blackhole) {
        repeat(count) {
            val result = singleSequenceOf(1).first()
            blackhole.consume(result)
        }
    }

    // Chain of transformations
    @Benchmark
    fun defaultSequenceOfSingleChain(blackhole: Blackhole) {
        repeat(count) {
            var seq = sequenceOf(1)
            repeat(operations) {
                seq = seq.map { it * 2 }
                    .filter { it > 0 }
            }
            blackhole.consume(seq.firstOrNull())
        }
    }

    @Benchmark
    fun singleSequenceOfChain(blackhole: Blackhole) {
        repeat(count) {
            var seq = singleSequenceOf(1)
            repeat(operations) {
                seq = seq.map { it * 2 }
                    .filter { it > 0 }
            }
            blackhole.consume(seq.firstOrNull())
        }
    }

    // Polymorphic callsite benchmark - mix different sequence types
    @Benchmark
    fun defaultSequenceOfPolymorphic(blackhole: Blackhole) {
        repeat(count) {
            val seq = if (it % 2 == 0) {
                sequenceOf(1)
            } else {
                sequenceOf(1, 2, 3)
            }
            blackhole.consume(seq.firstOrNull())
        }
    }

    @Benchmark
    fun mixedSequenceOfPolymorphic(blackhole: Blackhole) {
        repeat(count) {
            val seq = if (it % 2 == 0) {
                singleSequenceOf(1)
            } else {
                sequenceOf(1, 2, 3)
            }
            blackhole.consume(seq.firstOrNull())
        }
    }

    // Real-world scenarios
    @Benchmark
    fun defaultSequenceOfRealWorld(blackhole: Blackhole) {
        repeat(count) {
            val result = sequenceOf(it)
                .map { it * 2 }
                .filter { it > 0 }
                .map { it.toString() }
                .map { it.length }
                .sum()
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun singleSequenceOfRealWorld(blackhole: Blackhole) {
        repeat(count) {
            val result = singleSequenceOf(it)
                .map { it * 2 }
                .filter { it > 0 }
                .map { it.toString() }
                .map { it.length }
                .sum()
            blackhole.consume(result)
        }
    }
}