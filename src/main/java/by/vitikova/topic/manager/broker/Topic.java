package by.vitikova.topic.manager.broker;

import by.vitikova.topic.manager.exception.BrokerException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Представляет топик в системе брокера сообщений.
 * Топик может хранить сообщения и управлять потребителями.
 */
public class Topic {

    private final String name;
    private final List<Message> messageList = new ArrayList<>();
    private final Lock lock = new ReentrantLock(); // Блокировка для синхронизации
    private final Condition newMessageCondition = lock.newCondition(); // Условие для уведомления потребителей
    private final Semaphore semaphore; // Семафор для ограничения количества потребителей

    public Topic(String name, int maxConsumers) {
        this.name = name;
        this.semaphore = new Semaphore(maxConsumers);
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * Публикует новое сообщение в топик.
     * Уведомляет всех ожидающих потребителей, когда публикуется новое сообщение.
     *
     * @param messageContent содержимое сообщения для публикации
     */
    public void publishMessage(String messageContent) {
        lock.lock();
        try {
            Message message = new Message(messageContent);
            messageList.add(message);
            newMessageCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Потребляет сообщение из списка сообщений по заданному индексу.
     * <p>
     * Метод блокирует вызов, если список сообщений пуст, ожидание новых сообщений
     * осуществляется с использованием условия ожидания. При попытке получить сообщение
     * по некорректному индексу (отрицательному или выходящему за пределы списка)
     * метод возвращает {@code null}.
     *
     * @param index индекс сообщения, которое требуется получить из списка
     * @return {@link Message} сообщение по указанному индексу, или {@code null},
     * если индекс некорректен
     * @throws InterruptedException если поток ожидания был прерван
     * @throws BrokerException      если произошла ошибка во время обработки сообщения
     */
    public Message consumeMessage(int index) throws InterruptedException {
        try {
            semaphore.acquire();
            while (messageList.isEmpty()) {
                newMessageCondition.await();
            }
            if (index < 0 || index >= messageList.size()) {
                return null;
            }
            return messageList.get(index);
        } catch (Exception ex) {
            throw new BrokerException(ex.getMessage());
        } finally {
            semaphore.release();
        }
    }
}