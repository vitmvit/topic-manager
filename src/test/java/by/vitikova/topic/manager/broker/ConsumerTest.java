package by.vitikova.topic.manager.broker;

import by.vitikova.topic.manager.util.BrokerTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsumerTest {

    @Test
    public void consumerReadsAllMessages() throws InterruptedException {
        var topic = BrokerTestBuilder.builder().build().buildTopic();
        var expected = BrokerTestBuilder.builder().build().buildMessages();
        var producer = new Producer(topic, expected);
        var latch = new CountDownLatch(expected.size());
        var consumer = new Consumer(topic, latch);
        new Thread(producer).start();
        new Thread(consumer).start();

        latch.await();

        assertEquals(3, consumer.getMessageList().size());
        assertEquals(expected.get(0), consumer.getMessageList().get(0).getContent());
        assertEquals(expected.get(1), consumer.getMessageList().get(1).getContent());
        assertEquals(expected.get(2), consumer.getMessageList().get(2).getContent());
    }
}