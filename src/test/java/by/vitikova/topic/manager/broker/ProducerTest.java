package by.vitikova.topic.manager.broker;

import by.vitikova.topic.manager.util.BrokerTestBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProducerTest {

    @Test
    public void producerShouldPublishAllMessages() throws InterruptedException {
        var topic = BrokerTestBuilder.builder().build().buildTopic();
        var expectedMessageList = BrokerTestBuilder.builder().build().buildMessages();
        var producer = new Producer(topic, expectedMessageList);
        new Thread(producer).start();

        Thread.sleep(5000); // Даем время на публикацию

        assertEquals(3, topic.getMessageList().size());
        assertEquals(expectedMessageList.get(0), topic.getMessageList().get(0).getContent());
        assertEquals(expectedMessageList.get(1), topic.getMessageList().get(1).getContent());
        assertEquals(expectedMessageList.get(2), topic.getMessageList().get(2).getContent());
    }
}