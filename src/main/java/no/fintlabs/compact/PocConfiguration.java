package no.fintlabs.compact;

import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PocConfiguration {

    @Bean
    public EntityTopicNameParameters createTopic() {
        return EntityTopicNameParameters
                .builder()
                //.orgId("fint.no")
                //.domainContext("trond")
                .resource("my.student")
                .build();
    }



}
