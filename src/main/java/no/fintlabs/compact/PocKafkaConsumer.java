package no.fintlabs.compact;

import jakarta.annotation.PostConstruct;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonLoggingErrorHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PocKafkaConsumer {
    private final EntityConsumerFactoryService entityConsumerFactoryService;
    private final EntityTopicNameParameters topicNameParameters;
    private final AtomicInteger messagesCounter;
    private long highestCounter;

    public PocKafkaConsumer(EntityConsumerFactoryService entityConsumerFactoryService, EntityTopicNameParameters topicNameParameters) {
        this.entityConsumerFactoryService = entityConsumerFactoryService;
        this.topicNameParameters = topicNameParameters;
        messagesCounter = new AtomicInteger();
    }

    @PostConstruct
    private void createConsumer() {
        entityConsumerFactoryService.createFactory(
                Student.class,
                this::processEntity,
                new CommonLoggingErrorHandler()
        ).createContainer(topicNameParameters);


    }

    private void processEntity(ConsumerRecord<String, Student> record) {
        messagesCounter.incrementAndGet();
        long counter = record.value().getCounter();
        if (counter > highestCounter) highestCounter = counter;
    }

    public int getMessagesRecieved() {
        return messagesCounter.get();
    }

    public long getHighetsCounter() {
        return highestCounter;
    }


}
