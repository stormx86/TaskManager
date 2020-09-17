package pl.kozhanov.TaskManager.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TaskParserServiceTest {

    private TaskParserService taskParserService = new TaskParserService();

    private TaskParserService tps = mock(TaskParserService.class);

    private Gmail service = mock(Gmail.class);

    @Test
    void checkTask_ifGetService_shouldReturnTrue() throws GeneralSecurityException, IOException {
        Mockito.when(tps.getGmailService()).thenReturn(service);
        Mockito.when(tps.getListMessagesResponse(service)).thenReturn(new ListMessagesResponse());
        assertTrue(taskParserService.checkTask());
    }

    @Test
    void getHeader_ifHeaderName_shouldReturnHeaderValue() {
        List<MessagePartHeader> listMph = new ArrayList<>();
        MessagePartHeader mph = new MessagePartHeader();
        mph.setName("headerName");
        mph.setValue("headerValue");
        listMph.add(mph);
        assertEquals("headerValue", taskParserService.getSpecifiedHeader(listMph, "headerName"));
    }
}