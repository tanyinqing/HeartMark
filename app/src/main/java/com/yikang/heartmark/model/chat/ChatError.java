package com.yikang.heartmark.model.chat;

/**
 * Created by Chang on 12/29/2014.
 */
public class ChatError {

    private int errorType;
    private Exception exception;

    public ChatError(int errortype) {
        this.errorType = errortype;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public Exception getException() {
        return exception;
    }


    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {

        if (exception != null) {
            return "ChatError{" +
                    "errorType=" + errorType +
                    ", exception=" + exception.getMessage() +
                    '}';
        } else {
            return "ChatError{" +
                    "errorType=" + errorType +
                    '}';
        }
    }
}
