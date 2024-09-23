package by.vitikova.topic.manager.broker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Представляет потребителя в системе брокера сообщений.
 * Потребитель читает сообщения из указанного топика.
 */
public class Consumer implements Runnable {

    private final Topic topic; // Топик, из которого этот потребитель читает сообщения
    private final CountDownLatch latch; // Латч для управления количеством считанных сообщений
    private final List<Message> messageList; // Список для хранения прочитанных сообщений
    private int lastReadIndex; // Искомый индекс последнего прочитанного сообщения

    public Consumer(Topic topic, CountDownLatch latch) {
        this.topic = topic;
        this.latch = latch;
        this.lastReadIndex = 0;
        this.messageList = new ArrayList<>();
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * Выполняет цикл чтения сообщений из заданного топика.
     * Поток будет продолжать чтение сообщений, пока количество ожидаемых сообщений
     * в {@link CountDownLatch} не станет равным нулю.
     * <p>
     * В каждом цикле:
     * <ul>
     *     <li>Запрашивает сообщение по текущему индексу {@code lastReadIndex} из {@link Topic}.</li>
     *     <li>Если сообщение не равно {@code null}, то добавляет его в список прочитанных сообщений.</li>
     *     <li>Увеличивает индекс последнего прочитанного сообщения.</li>
     *     <li>Уменьшает счетчик в {@code latch}, что указывает на то, что одно сообщение было успешно прочитано.</li>
     * </ul>
     * <p>
     * Если поток прерывается во время ожидания, статус прерывания восстанавливается
     * с помощью {@code Thread.currentThread().interrupt()}.
     */
    @Override
    public void run() {
        try {
            while (latch.getCount() > 0) {
                Message message = topic.consumeMessage(lastReadIndex);
                if (message != null) {
                    messageList.add(message);
                    lastReadIndex++;
                    latch.countDown();
                    System.out.println("thread: " + Thread.currentThread().getName() + "; message: " + message.getContent() + "; messageList size: " + messageList.size());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}