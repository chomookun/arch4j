package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.message.model.Message;
import org.chomookun.arch4j.core.message.model.MessageSearch;
import org.chomookun.arch4j.core.message.MessageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/message")
@PreAuthorize("hasAuthority('admin.message')")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ModelAndView messages() {
        return new ModelAndView("admin/message.html");
    }

    @GetMapping("get-messages")
    @ResponseBody
    public Page<Message> getMessages(MessageSearch messageSearch, Pageable pageable) {
        return messageService.getMessages(messageSearch, pageable);
    }

    @GetMapping("get-message")
    @ResponseBody
    public Message getMessage(@RequestParam("messageId")String messageId) {
        return messageService.getMessage(messageId)
                .orElseThrow();
    }

    @PostMapping("save-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.message:edit')")
    public Message saveMessage(@RequestBody @Valid Message message) {
        return messageService.saveMessage(message);
    }

    @GetMapping("delete-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.message:edit')")
    public void deleteMessage(@RequestParam("messageId")String messageId) {
        messageService.deleteMessage(messageId);
    }

}
