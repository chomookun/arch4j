package org.chomookun.arch4j.web.view.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.client.NotifierClientFactory;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.notification.model.NotifierSearch;
import org.chomookun.arch4j.core.notification.NotifierService;
import org.chomookun.arch4j.core.notification.client.NotifierClientDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/notification")
@PreAuthorize("hasAuthority('admin.notification')")
@RequiredArgsConstructor
public class NotificationController {

    private final NotifierService notifierService;

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/notification");
        List<Notifier> notifiers = notifierService.getNotifiers(NotifierSearch.builder().build(), Pageable.unpaged()).getContent();
        modelAndView.addObject("notifiers", notifiers);
        modelAndView.addObject("notificationStatuses", Notification.Status.values());
        modelAndView.addObject("notifierClientDefinitions", NotifierClientDefinitionRegistry.getNotifierClientDefinitions());
        return modelAndView;
    }

    @GetMapping("get-notifications")
    @ResponseBody
    public Page<Notification> getNotifications(NotificationSearch notificationSearch, Pageable pageable) {
        return notificationService.getNotifications(notificationSearch, pageable);
    }

    @PostMapping("send-notification")
    @ResponseBody
    public void sendNotification(@RequestBody Notification notification) throws JsonProcessingException {
        notificationService.sendNotification(notification.getNotifierId(), notification.getTo(), notification.getSubject(), notification.getContent(), null);
    }

    @GetMapping("get-notifiers")
    @ResponseBody
    public Page<Notifier> getNotifiers(NotifierSearch notifierSearch, Pageable pageable) {
        return notifierService.getNotifiers(notifierSearch, pageable);
    }

    @GetMapping("get-notifier")
    @ResponseBody
    public Notifier getNotifier(@RequestParam("notifierId")String notifierId) {
        return notifierService.getNotifier(notifierId)
                .orElseThrow();
    }

    @PostMapping("save-notifier")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.notification:edit')")
    public Notifier saveNotifier(@RequestBody @Valid Notifier notifier) {
        return notifierService.saveNotifier(notifier);
    }

    @GetMapping("delete-notifier")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.notification:edit')")
    public void deleteNotifier(@RequestParam("notifierId")String notifierId) {
        notifierService.deleteNotifier(notifierId);
    }

    @PostMapping("test-notifier")
    @ResponseBody
    public void testNotifier(@RequestBody JsonNode jsonNode) throws JsonProcessingException {
        JsonNode notifierNode = jsonNode.get("notifier");
        Notifier notifier = objectMapper.treeToValue(notifierNode, Notifier.class);
        String to = jsonNode.get("to").asText();
        String subject = jsonNode.get("subject").asText();
        String content = jsonNode.get("content").asText();
        notifierService.testNotifier(notifier, to, subject, content);
    }

}
