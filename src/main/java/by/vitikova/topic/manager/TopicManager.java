package by.vitikova.topic.manager;

import by.vitikova.topic.manager.broker.Consumer;
import by.vitikova.topic.manager.broker.Producer;
import by.vitikova.topic.manager.broker.Topic;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TopicManager {

    public static void main(String[] args) throws InterruptedException {
        Topic topic = new Topic("MyTopic", 3);
        List<String> messagesToProduce = List.of(
                "Message 1",
                "Message 2",
                "Message 3",
                "Message 4",
                "Message 5",
                "Message 6",
                "Message 7",
                "Message 8",
                "Message 9",
                "Message 10"
        );
        Producer producer = new Producer(topic, messagesToProduce);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        CountDownLatch latch = new CountDownLatch(30);

        new Thread(new Consumer(topic, latch)).start();
        new Thread(new Consumer(topic, latch)).start();
        new Thread(new Consumer(topic, latch)).start();

        latch.await();
    }
}