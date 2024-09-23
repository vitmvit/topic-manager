package by.vitikova.topic.manager.broker;

import by.vitikova.topic.manager.util.BrokerTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TopicTest {

    private Topic topic;

    @BeforeEach
    public void setUp() {
        topic = BrokerTestBuilder.builder().build().buildTopic();
    }

    @Test
    public void publishMessageShouldPublicMessage() {
        var expected = "Hello";
        topic.publishMessage(expected);

        assertEquals(1, topic.getMessageList().size());
        assertEquals(expected, topic.getMessageList().get(0).getContent());
    }

    @Test
    public void consumeMessageShouldReturnMessage() throws InterruptedException {
        var expected = "Hello";

        topic.publishMessage(expected);
        var actual = topic.consumeMessage(0);

        assertNotNull(actual);
        assertEquals(expected, actual.getContent());
    }

    @Test
    public void consumeMessageShouldReturnNullWhereInvalidIndex() throws InterruptedException {
        var expected = "Hello";

        topic.publishMessage(expected);
        var actual = topic.consumeMessage(1);

        assertNull(actual);
    }

    @Test
    public void consumeMessageShouldAwaitWhenNoMessages() throws InterruptedException {
        var expected = "Hello";

        Thread.sleep(1000);
        topic.publishMessage(expected);

        var actual = topic.consumeMessage(0);

        assertNotNull(actual);
        assertEquals(expected, actual.getContent());
    }
}