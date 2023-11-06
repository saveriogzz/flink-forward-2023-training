package com.rockthejvm.part1streams;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Iterator;

public class EssentialStreams {

  public static void applicationTemplate() throws Exception {
    // execution env
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // build some data streams
    DataStream<Integer> numbers = env.fromElements(1,2,3,4,5);
    // some more of these

    // data transformations

    // call an action on the DS
    numbers.print();
    // potentially multiple things

    env.execute(); // starts the stuff
  }

  // DS transformations
  public static void demoTransformations() throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStream<Long> numbers = env.fromSequence(1, 100);

    // check the parallelism at this point
    System.out.println("Current parallelism: " + env.getParallelism());
    // set parallelism
    env.setParallelism(2);

    // map
    DataStream<Long> numbersx2 = numbers.map(new MapFunction<Long, Long>() {
      @Override
      public Long map(Long aLong) throws Exception {
        return aLong * 2;
      }
    });

    DataStream<String> numbers2Strings = numbers.map(new MapFunction<Long, String>() {
      @Override
      public String map(Long aLong) throws Exception {
        return "Flink " + aLong;
      }
    });

    DataStream<Long> numbersx2_v2 = numbers.map(e -> e * 2);

    // flatMap
    DataStream<Long> expandedNumbers = numbers.flatMap(new FlatMapFunction<Long, Long>() {
      @Override
      public void flatMap(Long number, Collector<Long> collector) throws Exception {
        // you can push as many items as you want
        collector.collect(number);
        collector.collect(number * 1000);
      }
    });

    // filter
    DataStream<Long> evenNumbers = numbers.filter(e -> e % 2 == 0);

    // consume the DSs as you like
    evenNumbers.sinkTo(
      FileSink.forRowFormat(
        new Path("output/streaming_sink"),
        new SimpleStringEncoder<Long>("UTF-8")
      ).build()
    );

    env.execute();
  }

  /**
   *  Exercise - fizz buzz
   *  - a stream of 100 numbers
   *  - return a DS of strings - for every number
   *    - return "fizz" if number % 3
   *    - "buzz" if number % 5
   *    - "fizzbuzz" if both
   *  - write the strings to a file (single file)
   *  - write just the fizzbuzz strings to another file
   */




  public static void main(String[] args) throws Exception {
    demoTransformations();
  }
}