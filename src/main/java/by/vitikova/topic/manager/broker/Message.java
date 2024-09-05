package by.vitikova.topic.manager.broker;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Представляет сообщение в системе брокера сообщений.
 */
@Getter
@AllArgsConstructor
public class Message {

    private String content;
}
