package org.chomookun.arch4j.web.view.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.notification.model.NotificationMessageSearch;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.client.NotificationClientDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

import java.util.Map;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/notification")
@PreAuthorize("hasAuthority('admin.notification')")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationMessageService notificationMessageService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/notification");
        modelAndView.addObject("notificationClientDefinitions", NotificationClientDefinitionRegistry.getNotificationClientDefinitions());
        modelAndView.addObject("notificationMessageStatuses", NotificationMessage.Status.values());
        return modelAndView;
    }

    @GetMapping("get-notifications")
    @ResponseBody
    public Page<Notification> getNotifications(NotificationSearch notificationSearch, Pageable pageable) {
        return notificationService.getNotifications(notificationSearch, pageable);
    }

    @GetMapping("get-notification")
    @ResponseBody
    public Notification getNotification(@RequestParam("notificationId")String notificationId) {
        return notificationService.getNotification(notificationId)
                .orElseThrow();
    }

    @PostMapping("save-notification")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.notification:edit')")
    public Notification saveNotification(@RequestBody @Valid Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @GetMapping("delete-notification")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.notification:edit')")
    public void deleteNotification(@RequestParam("notificationId")String notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @PostMapping("send-notification-message")
    @ResponseBody
    public void sendNotificationMessage(@RequestBody JsonNode jsonNode) throws JsonProcessingException {
        JsonNode notificationNode = jsonNode.get("notification");
        Notification notification = objectMapper.treeToValue(notificationNode, Notification.class);
        String to = jsonNode.get("to").asText();
        String subject = jsonNode.get("subject").asText();
        String content = jsonNode.get("content").asText();
        boolean test = jsonNode.get("test").asBoolean(false);
        if (test) {
            notificationService.testNotification(notification, to, subject, content);
        } else {
            notificationMessageService.sendNotificationMessage(notification, to, subject, content, null);
        }
    }

    @GetMapping("get-notification-messages")
    @ResponseBody
    public Page<NotificationMessage> getNotificationMessages(NotificationMessageSearch notificationMessageSearch, Pageable pageable) {
        return notificationMessageService.getNotificationMessages(notificationMessageSearch, pageable);
    }

}
