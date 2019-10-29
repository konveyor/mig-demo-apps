/**
 *
 */
package es.amq.consumer.routes;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author rmarting
 */
@Component
public class ConsumerRouteBuilder extends RouteBuilder {

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {

		from("amq:queue:producer-queue").id("consumer").log("Receiving message from queue amq:queue:producer-queue")
				.log("Message from producer ${headers['producer-id']}: ${body}")
				.bean("parseBean", "updateState(${headers['producer-id']}, ${body})")
				.log("Message from producer ${headers['producer-id']} consumed");
	}
}
