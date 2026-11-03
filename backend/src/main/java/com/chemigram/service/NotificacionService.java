
package com.chemigram.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificacionService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notificarGremio(Long gremioId, Object mensaje) {

        messagingTemplate.convertAndSend(
                "/topic/gremio/" + gremioId,
                mensaje
        );
    }

    public void notificarUsuario(String email, Object mensaje) {

        messagingTemplate.convertAndSendToUser(
                email,
                "/queue/notificaciones",
                mensaje
        );
    }
}
