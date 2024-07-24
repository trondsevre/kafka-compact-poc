package no.fintlabs.compact;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class KafkaTopicConfigUtils {

    private final String topicName = "fintlabs-no.poc.entity.my-student";

    private final AdminClient adminClient;

    public KafkaTopicConfigUtils(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @PostConstruct
    public void getMaxCompactionLagMs() throws ExecutionException, InterruptedException {

        updateMaxCompationLagMs("60000");

        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(configResource));

        Config config = describeConfigsResult.all().get().get(configResource);

        for (ConfigEntry entry : config.entries()) {
            if (entry.name().equals("max.compaction.lag.ms")) {
                log.info("{}: {}", entry.name(), entry.value());
            }
        }
    }

    // 9223372036854775807
    public void updateMaxCompationLagMs(String newValue) throws ExecutionException, InterruptedException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        ConfigEntry configEntry = new ConfigEntry("max.compaction.lag.ms", newValue);
        Config config = new Config(Collections.singleton(configEntry));

        AlterConfigsResult alterConfigsResult = adminClient.alterConfigs(Collections.singletonMap(configResource, config));

        alterConfigsResult.all().get();
    }
}
