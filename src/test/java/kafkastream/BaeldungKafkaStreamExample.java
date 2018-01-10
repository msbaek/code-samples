package kafkastream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public class BaeldungKafkaStreamExample {
    public static void main(String[] args) {
        String inputTopic = "inputTopic";
        String bootstrapServers = "localhost:9092";
        String tempDirectory = "/tmp";

        Properties streamsConfiguration = new Properties();

        // define our data source and name of our application using APPLICATION_ID_CONFIG:
        // application ID = kafka consumer group
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-live-test");

        // BOOTSTRAP_SERVER_CONFIG. This is the URL to our local Kafka instance
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        StreamsConfig.ZOOKEEPER_CONNECT_CONFIG
//        StreamsConfig.NUM_STREAM_THREADS_CONFIG // threads for parallel processing(relates to paritions)

        // type of the key and value of messages that will be consumed from inputTopic
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Stream processing is often stateful.
        // When we want to save intermediate results, we need to specify the STATE_DIR_CONFIG parameter.
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, tempDirectory);

        /**
         * Building a Streaming Topology
         *
         * Once we defined our input topic, we can create a Streaming Topology –
         * that is a definition of how events should be handled and transformed.
         */
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> textLines = builder.stream(inputTopic);
        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

        KTable<String, Long> wordCounts = textLines
                .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
                .groupBy((key, word) -> word)
                .count();

        wordCounts.foreach((w, c) -> System.out.println("wordCount for word: " + w + " -> " + c));
        /**
         * On production, often such streaming job might publish the output to another Kafka topic.
         * We could do this using the to() method:
         *
         * String outputTopic = "outputTopic";
         *
         * // Serde class gives us preconfigured serializers for Java types
         * // that will be used to serialize objects to an array of bytes.
         * Serde<String> stringSerde = Serdes.String();
         * Serde<Long> longSerde = Serdes.Long();
         *
         * // to() method will save the resulting data to outputTopic.
         * wordCounts.to(stringSerde, longSerde, outputTopic);
         */


        /**
         * Starting KafkaStream Job
         *
         * Up to this point, we built a topology that can be executed. However, the job hasn’t started yet.
         * We need to start our job explicitly by calling the start() method on the KafkaStreams instance:
         */
        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.start();

        try {
            Thread.sleep(1_000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        streams.close();
    }
}
