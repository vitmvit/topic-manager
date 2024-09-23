package by.vitikova.topic.manager.broker;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Представляет производителя в системе брокера сообщений.
 * Производитель публикует сообщения в указанный топик.
 */
@AllArgsConstructor
public class Producer implements Runnable {

    private final Topic topic; // Топик, в который публикуются сообщения
    private final List<String> messageList; // Список сообщений для публикации

    /**
     * Выполняет цикл публикации сообщений в заданный топик {@link Topic}.
     * Для каждого сообщения из списка {@code messageList}:
     * <ul>
     *     <li>Публикует сообщение, используя метод {@code publishMessage}.</li>
     *     <li>Симулирует задержку в 1000 миллисекунд (1 секунда) между публикациями.</li>
     * </ul>
     * <p>
     * Если поток прерывается во время ожидания, статус прерывания
     * восстанавливается с помощью {@code Thread.currentThread().interrupt()}.
     */
    @Override
    public void run() {
        for (String messageContent : messageList) {
            topic.publishMessage(messageContent);
            System.out.println("publish message: " + messageContent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
