package pl.kozhanov.taskmanager.domain;

public class Attachment {

    private byte[] body;

    private String fileName;

    public Attachment(byte[] body, String fileName) {
        this.body = body;
        this.fileName = fileName;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
