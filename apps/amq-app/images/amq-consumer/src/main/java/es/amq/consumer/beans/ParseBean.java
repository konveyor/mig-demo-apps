package es.amq.consumer.beans;

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
@Component(value = "parseBean")
public class ParseBean {

	private static final Logger logger = LoggerFactory.getLogger(ParseBean.class);

	public void updateState(String id, String data) {
		compareAndIncrement("/var/state/producers-state-" + id, data);
	}

	private void compareAndIncrement(String filename, String data) {
		try {
			Path stateFile = Paths.get(filename);
			if (!Files.exists(stateFile)) {
				Files.write(stateFile, data.getBytes(), StandardOpenOption.CREATE);
				logger.info("Stored new state file: " + filename);
				return;
			}

			String expected = Files.readAllLines(stateFile).get(0);
			Integer expectedCount = Integer.valueOf(expected);
			Integer receivedCount = Integer.valueOf(data);
			expectedCount++;
			if (!receivedCount.equals(expectedCount)) {
				logger.error("Unexpected state number: Expected - " + expectedCount + ", Received - " + receivedCount
						+ " File: " + filename);
			}

			// Updating expected to follow incoming counter
			Files.write(stateFile, data.getBytes());
			logger.info("Updated " + filename + " to " + expected);
		} catch (Exception e) {
			logger.error("Errors reading file", e);
		}
	}

}
