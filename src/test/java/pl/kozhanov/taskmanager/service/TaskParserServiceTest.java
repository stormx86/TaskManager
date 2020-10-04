package pl.kozhanov.taskmanager.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class TaskParserServiceTest {

    private TaskParserService tps = mock(TaskParserService.class);

    private Gmail service = mock(Gmail.class);

    private TaskParserService taskParserService = new TaskParserService();


    @Test
    void checkTask_ifGetServiceOk_shouldReturnTrue() throws IOException {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());
        ListMessagesResponse lmr = new ListMessagesResponse();
        lmr.setMessages(messages);
        Mockito.when(tps.getListMessagesResponse(any(Gmail.class))).thenReturn(lmr);
        Mockito.when(tps.checkTask(service)).thenCallRealMethod();
        assertTrue(tps.checkTask(service));
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