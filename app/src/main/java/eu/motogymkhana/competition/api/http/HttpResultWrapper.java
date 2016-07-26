package eu.motogymkhana.competition.api.http;

public class HttpResultWrapper {

    private int statusCode;
    private String string;
    private String errorMessage;

    public HttpResultWrapper(int status, String msg, String string) {

        this.statusCode = status;
        this.string = string;
        this.errorMessage = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
