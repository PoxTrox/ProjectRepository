package org.example.projectRepository.scheduler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class messageToTheUser {

    private final SimpMessagingTemplate messagingTemplate;



    public messageToTheUser(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 600000)
    public void sendReminder() {
        messagingTemplate.convertAndSend("/topic/reminders", "Please take a break! 🌿");
    }
}
