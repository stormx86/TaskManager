package pl.kozhanov.TaskManager.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public interface TaskViewProjection {

    Integer getId();

    Instant getReceivedAt();

    String getSentBy();

    String getSubject();

    String getSnippet();

    String getStatus();

    String getEditBy();

    boolean getHasAttachment();

    String getMessageId();

    default String getReceivedAtFormatted() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(ZonedDateTime.ofInstant(getReceivedAt(), ZoneId.systemDefault()));
    }
}
