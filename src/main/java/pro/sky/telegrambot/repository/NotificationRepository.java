package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Query(nativeQuery = true,
    value = "select notification_id from notification where to_char(notification_data,'YYYY-MM-DD HH24:MI') = :date")
    List<Long> getNotificationWhereDataEqualsCurrent(String date);

}