package com.via.letmein.persistence.api;

import com.google.gson.annotations.Expose;

/**
 * Represents the response from the server with a variable content attribute.
 * The content attribute needs to be cast to be usable, based on the expected value of it.
 * For specifics refer to the <a href="https://github.com/ivancolomer/api-reac-android">API documentation</a>
 * All responses from the server are 200 OK.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class ApiResponse {
    /**
     * Error indicator
     */
    @Expose
    private boolean error;

    /**
     * Error description, null if there's no error
     */
    @Expose
    private String errorMessage;

    /**
     * Response's content, 0 if error is true
     */
    @Expose
    private Object content = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
