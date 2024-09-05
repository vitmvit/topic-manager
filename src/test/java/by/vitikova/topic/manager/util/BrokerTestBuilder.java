package by.vitikova.topic.manager.util;

import by.vitikova.topic.manager.broker.Topic;
import lombok.Builder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Builder(setterPrefix = "with")
public class BrokerTestBuilder {

    @Builder.Default
    private String nameTopic = "TestTopic";

    @Builder.Default
    private int maxConsumers = 3;

    @Builder.Default
    private CountDownLatch latch = new CountDownLatch(1);

    @Builder.Default
    private List<String> messageList = Arrays.asList("Msg1", "Msg2", "Msg3");

    public Topic buildTopic() {
        return new Topic(nameTopic, maxConsumers);
    }

    public List<String> buildMessages() {
        return messageList;
    }
}