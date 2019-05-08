package com.example.chat.db.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;


@Entity
@Table(name = "massages")
public class Message {

    @ManyToOne
    private User user;
	
    private String body;

    @Column(updatable=false, name = "createdAt")
    private Timestamp createdAt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;
    
    public User getUser() {
		return user;
	}
    
    public void setUser(User user) {
		this.user = user;
	}
    
    @PrePersist
    public void prePersist() {
        Timestamp ts = new Timestamp((new Date()).getTime());
        this.createdAt = ts;
    }

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}
    
    
}
