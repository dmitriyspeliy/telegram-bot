package pro.sky.telegrambot.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notification", schema = "public")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "notification_text")
    private String notificationText;
    @Column(name = "notification_data")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime notificationData;


    public Notification() {
    }

    public Notification(Long chatId, String notificationText, LocalDateTime notificationData) {
        this.chatId = chatId;
        this.notificationText = notificationText;
        this.notificationData = notificationData;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public LocalDateTime getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(LocalDateTime notificationData) {
        this.notificationData = notificationData;
    }
}
