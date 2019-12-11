/**
 *
 */
package es.amq.producer.routes;

import java.util.Random;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author rmarting
 */
@Component
public class ProducerRouteBuilder extends RouteBuilder {

	@Value("${producers.number}")
	private int producers;

	/**
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {

		Random r = new Random(1000);

		for (int i = 0; i < producers; i++) {
			from("timer:bm-amq-producer?period=" + Math.abs(r.nextInt(20) + 5) + "s").id("producer-trigger-" + i).setBody()
					.method("loadBodyBean", "loadAndIncrement(" + i + ")").to("amq:queue:trigger-prod-" + i);

			from("amq:queue:trigger-prod-" + i).id("producer-" + i).setHeader("producer-id").simple(String.valueOf(i))
					.log("Sending message to queue producer-queue from prod " + i).to("amq:queue:producer-queue")
					.log("Sent message to queue producer-queue");

		}
	}

}
