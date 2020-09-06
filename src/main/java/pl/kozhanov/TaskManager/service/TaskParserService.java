package pl.kozhanov.TaskManager.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import org.springframework.stereotype.Component;
import pl.kozhanov.TaskManager.domain.Task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TaskParserService {


    private static final String APPLICATION_NAME = "Task Manager";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String USER_ID = "me";
    private static final String QUERY = "in:inbox is:unread";



    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_MODIFY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = TaskParserService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public List<Task> getTask() throws IOException, GeneralSecurityException {
        Gmail service = getGmailService();
        List<Task> tasks = new ArrayList<>();
        ListMessagesResponse response = getListMessagesResponse(service);
        List<Message> messages = response.getMessages();
        if (messages != null) {
            for (Message message : messages) {
                service.users().messages().modify(USER_ID, message.getId(), new ModifyMessageRequest().setRemoveLabelIds(new ArrayList<>(Arrays.asList("UNREAD")))).execute();
                Message newMessage = service.users().messages().get(USER_ID, message.getId()).execute();
                tasks.add(formTask(newMessage));
            }
        }
        return tasks;
    }

    public Task formTask(Message newMessage) {
        List<MessagePartHeader> headers = newMessage.getPayload().getHeaders();
        String subject = "";
        Instant receivedAt = Instant.ofEpochMilli(newMessage.getInternalDate());
        String sentBy = getHeader(headers, "From");
        if (getHeader(headers, "Subject") != null) {
            subject = getHeader(headers, "Subject");
        } else {
            subject = "No Subject";
        }
        String snippet = newMessage.getSnippet();
        String status = "Waiting";
        String editBy = "";
        return new Task(receivedAt, sentBy, subject, snippet, status, editBy);
    }

    public Gmail getGmailService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public boolean checkTask() throws IOException, GeneralSecurityException {
        Gmail service = getGmailService();
        ListMessagesResponse response = getListMessagesResponse(service);
        return (response.getMessages() != null);
    }

    public ListMessagesResponse getListMessagesResponse(Gmail service) throws IOException {
        return service.users().messages().list(USER_ID).setQ(QUERY).execute();
    }


    public String getHeader(List<MessagePartHeader> headers, String name) {
        for (MessagePartHeader h : headers) {
            if (h.getName().equals(name)) {
                return h.getValue();
            }
        }
        return "no header";
    }
}
