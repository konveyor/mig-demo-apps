package es.amq.producer.beans;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A bean which we use in the route
 */
@Component(value = "loadBodyBean")
public class LoadBodyBean {

	private static final Logger logger = LoggerFactory.getLogger(LoadBodyBean.class);

	public String loadAndIncrement(Integer id) {
		return increment("/var/state/producers-state-" + id);
	}

	private String increment(String filename) {
		String data = "-1";
		try {
			Path stateFile = Paths.get(filename);
			if (!Files.exists(stateFile)) {
				Files.write(stateFile, "0".getBytes(), StandardOpenOption.CREATE);
				logger.info("Created state file: " + filename);
			}

			data = Files.readAllLines(stateFile).get(0);
			Integer counter = Integer.valueOf(data);
			counter++;
			data = Integer.toString(counter);
			Files.write(stateFile, data.getBytes());
			logger.info("Updated " + filename + " to " + data);
		} catch (Exception e) {
			logger.error("Errors reading file", e);
		}
		return data;
	}

}
