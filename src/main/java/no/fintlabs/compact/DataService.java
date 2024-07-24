package no.fintlabs.compact;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class DataService {

    private final PocKafkaProducer pocKafkaProducer;
    private final PocKafkaConsumer pocKafkaConsumer;
    private final PocKafkaTopicCounter topicCounter;
    private AtomicInteger counter;

    public DataService(PocKafkaProducer pocKafkaProducer, PocKafkaConsumer pocKafkaConsumer, PocKafkaTopicCounter topicCounter) {
        this.pocKafkaProducer = pocKafkaProducer;
        this.pocKafkaConsumer = pocKafkaConsumer;
        this.topicCounter = topicCounter;
        counter = new AtomicInteger();
    }

    @PostConstruct
    private void testDataGeneration() {

    }

    @Scheduled(initialDelay = 1L, fixedDelay = 1L, timeUnit = TimeUnit.SECONDS)
    private void addData(){
        for (int i = 0; i < 99; i++) {
            pocKafkaProducer.sendMessage(createStudent(i));
        }

        log.info("100 records added to kafka");
    }

    @Scheduled(initialDelay = 1L, fixedDelay = 10L, timeUnit = TimeUnit.SECONDS)
    private void readData(){
        log.info("Consumer: {} entities recieved, {} highest counter", pocKafkaConsumer.getMessagesRecieved(), pocKafkaConsumer.getHighetsCounter());
    }

    @Scheduled(initialDelay = 2L, fixedDelay = 10L, timeUnit = TimeUnit.SECONDS)
    private void countData(){
        log.info("Consumer contains: {} entities", topicCounter.count());
    }

    private Student createStudent(int id) {
        Student newStudent = new Student();
        newStudent.setId(id);
        newStudent.setCorrId(UUID.randomUUID().toString());
        newStudent.setCounter(counter.incrementAndGet());
        return newStudent;
    }
}
