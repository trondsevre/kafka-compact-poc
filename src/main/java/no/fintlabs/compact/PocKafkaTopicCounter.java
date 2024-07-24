package no.fintlabs.compact;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.kafka.entity.EntityConsumerConfiguration;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class PocKafkaTopicCounter {

    private final EntityTopicNameParameters topicNameParameters;
    private final EntityConsumerFactoryService entityConsumerFactoryService;
    private boolean isBusy;
    private AtomicLong counter;

    public PocKafkaTopicCounter(EntityTopicNameParameters topicNameParameters, EntityConsumerFactoryService entityConsumerFactoryService) {
        this.topicNameParameters = topicNameParameters;
        this.entityConsumerFactoryService = entityConsumerFactoryService;
        counter = new AtomicLong();
    }

    public long count() {

        if (isBusy) throw new IllegalStateException("Allready at work");

        isBusy = true;
        counter.set(0L);

        entityConsumerFactoryService.createFactory(
                Student.class,
                this::processEntity,
                EntityConsumerConfiguration.builder()
                        .groupIdSuffix(UUID.randomUUID().toString())
                        .build()
        ).createContainer(
                topicNameParameters
        );

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            log.error("Error in sleep", e);
        }

        isBusy = false;
        return counter.get();
    }

    private void processEntity(ConsumerRecord<String, Student> stringStudentConsumerRecord) {
        counter.incrementAndGet();
    }
}
