package com.example.chat.entity;

public class SocketMessage {
    private String body;

    private String username;

    private String createdAt;

    private Long chatId;

    public SocketMessage() {
    }

    public SocketMessage(String body, String username, String createdAt, Long chatId) {
        this.body = body;
        this.username = username;
        this.createdAt = createdAt;
        this.chatId = chatId;
    }

    public String getBody() {
        return body;
    }

    public String getUsername() {
        return username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long getChatId() {
        return chatId;
    }

}
