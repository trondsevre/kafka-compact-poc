package no.fintlabs.compact;

import no.fintlabs.kafka.entity.EntityProducer;
import no.fintlabs.kafka.entity.EntityProducerFactory;
import no.fintlabs.kafka.entity.EntityProducerRecord;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

@Service
public class PocKafkaProducer {

    public static final long retentionTimeMs = 1000 * 60 * 60;

    private final EntityProducer<Student> producer;
    private final EntityTopicNameParameters topicNameParameters;

    public PocKafkaProducer(EntityProducerFactory entityProducerFactory, EntityTopicService entityTopicService, EntityTopicNameParameters topicNameParameters) {
        producer = entityProducerFactory.createProducer(Student.class);
        this.topicNameParameters = topicNameParameters;

        entityTopicService.ensureTopic(
                topicNameParameters,
                retentionTimeMs
        );
    }

    public void sendMessage(Student student) {
        EntityProducerRecord<Student> record = new EntityProducerRecord<>();
        record.setTopicNameParameters(topicNameParameters);
        record.setKey(String.valueOf(student.getId()));
        record.setValue(student);
        producer.send(record);
    }
}
