package pl.kozhanov.TaskManager.domain;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TaskParser {


    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String userId = "me";
    private static String messageId = "1723e0ae62dd5d95";
    private static String query = "in:inbox is:unread";
    private String subject = "";


    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_MODIFY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = TaskParser.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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
        List<Task> tasks = new ArrayList<Task>();

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();
        List<Message> messages = response.getMessages();
        if(messages != null) {
            for (Message message : messages) {
                service.users().messages().modify(userId, message.getId(), new ModifyMessageRequest().setRemoveLabelIds(new ArrayList<String>(Arrays.asList("UNREAD")))).execute();

                Message message2 = service.users().messages().get(userId, message.getId()).execute();
                List<MessagePartHeader> headers = message2.getPayload().getHeaders();
                LocalDateTime receivedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(message2.getInternalDate()), ZoneId.systemDefault());
                String sentBy = getHeader(headers, "From");
                if(getHeader(headers, "Subject")!=null){
                    subject = getHeader(headers, "Subject");
                }
                else{
                    subject = "No Subject";
                }
                String snippet = message2.getSnippet();
                String status = "Waiting";
                String editBy = "";
                tasks.add(new Task(receivedAt, sentBy, subject, snippet, status, editBy));
            }
        }
        return tasks;
    }

    public boolean checkTask() throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();
        if(response.getMessages() != null){
            return true;
        }
            return false;
    }


    public static String getHeader(List<MessagePartHeader> headers, String name ){
        for(MessagePartHeader h : headers) {
            if(h.getName().equals(name)) {
                return h.getValue();
            }
        }
        return null;
    }
}
