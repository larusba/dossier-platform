package it.larus.bigdata.dossier.crawler.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class FileMockCrawler {

	public static void main(String[] args) throws IOException {

		String server = args[0];
		String topic = args[1];
		String directory = args[2];

		Map<String, Object> config = new HashMap<>();
		// config.put("bootstrap.servers", "localhost:9092");
		config.put("bootstrap.servers", server);
		config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

		//FIXME handle errors
		
		Files.list(Paths.get(directory)).forEach(p -> {
			String content;
			try {
				System.out.println("Sending "+p);
				content = new String(Files.readAllBytes(p));
				producer.send(new ProducerRecord<String, String>(topic, content));
			} catch (IOException e) {
				System.err.println("Discard: "+p);
			}

		});

		System.out.println("Flushing...");
		producer.flush();
		System.out.println("Closing...");
		producer.close();
	}

}
