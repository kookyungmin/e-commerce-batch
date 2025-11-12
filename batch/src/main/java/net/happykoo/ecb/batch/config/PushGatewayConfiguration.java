package net.happykoo.ecb.batch.config;

import io.prometheus.client.exporter.PushGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PushGatewayConfiguration {

  @Bean
  public PushGateway pushGateway(
      @Value("${prometheus.push-gate-way.address:localhost:19091}") String address) {
    return new PushGateway(address);
  }
}
