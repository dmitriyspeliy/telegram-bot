package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final NotificationRepository notificationRepository;

    private final Pattern FINDDATA = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationRepository notificationRepository) {
        this.telegramBot = telegramBot;
        this.notificationRepository = notificationRepository;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Matcher matcher = FINDDATA.matcher(update.message().text());
            if (update.message().text().equals("/start")) {
                SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Hello");
                telegramBot.execute(sendMessage);
            } else if (matcher.find()) {
                notificationRepository.save(new Notification(update.message().chat().id(), matcher.group(3),
                        LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public List<Long> findDataFromDBWithTimeNow(){
        return notificationRepository.getNotificationWhereDataEqualsCurrent(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()
                .replace("T"," "));
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void run(){
        if(!findDataFromDBWithTimeNow().isEmpty()){
            List<Notification> notificationList = notificationRepository.findAllById(findDataFromDBWithTimeNow());
            for (Notification notification : notificationList) {
                SendMessage sendMessage = new SendMessage(notification.getChatId()
                        , "Задание " + notification.getNotificationText() + "!. Его нужно выполнить");
                telegramBot.execute(sendMessage);
            }
        }
    }

}
