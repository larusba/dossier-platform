package it.larus.bigdata.dossier.analyzer;

import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * --topic news-topic --bootstrap.servers localhost:9092 --zookeeper.connect
 * localhost:2181 --group.id myGroup --tokenfile en-token.bin --namefile en-ner-person.bin
 * 
 * @author Omar Rampado
 *
 */
public class FlinkNewsAnalyzer {

	public static void main(String[] args) throws Exception {
		// FIXME handle errors

		// create execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// parse user parameters
		ParameterTool parameterTool = ParameterTool.fromArgs(args);

		DataStream<String> messageStream = env.addSource(new FlinkKafkaConsumer082<>(parameterTool.getRequired("topic"),
				new SimpleStringSchema(), parameterTool.getProperties()));

		final NaturalLanguageProcessor languageProcessor = new NaturalLanguageProcessor(parameterTool.getRequired("tokenfile"),parameterTool.getRequired("namefile"));

		messageStream.rebalance().map(new MapFunction<String, String>() {
			private static final long serialVersionUID = -1;

			private ObjectMapper mapper = new ObjectMapper();

			@Override
			public String map(String value) throws Exception {

				Map<String, String> map = mapper.readValue(value, new TypeReference<Map<String, Object>>() {
				});
				String body = map.get("body_text");

				return "News title: " + map.get("title") + " => " + languageProcessor.extractNames(body);
			}
		}).print();

		env.execute();
	}

}
