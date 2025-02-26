package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.message.model.Message;
import org.chomookun.arch4j.core.message.model.MessageSearch;
import org.chomookun.arch4j.core.message.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/messages")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin.messages')")
public class MessagesController {

    private final MessageService messageService;

    /**
     * Returns messages model and view
     * @return model and view
     */
    @GetMapping
    public ModelAndView messages() {
        return new ModelAndView("admin/messages.html");
    }

    /**
     * Returns messages
     * @param messageSearch message search
     * @param pageable pageable
     * @return page of message
     */
    @GetMapping("get-messages")
    @ResponseBody
    public Page<Message> getMessages(MessageSearch messageSearch, Pageable pageable) {
        return messageService.getMessages(messageSearch, pageable);
    }

    /**
     * Returns specified message
     * @param messageId message id
     * @return message
     */
    @GetMapping("get-message")
    @ResponseBody
    public Message getMessage(@RequestParam("messageId")String messageId) {
        return messageService.getMessage(messageId)
                .orElseThrow();
    }

    /**
     * Saves message
     * @param message message
     * @return saved message
     */
    @PostMapping("save-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.messages.edit')")
    public Message saveMessage(@RequestBody @Valid Message message) {
        return messageService.saveMessage(message);
    }

    /**
     * Deletes message
     * @param messageId message id
     */
    @GetMapping("delete-message")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.messages.edit')")
    public void deleteMessage(@RequestParam("messageId")String messageId) {
        messageService.deleteMessage(messageId);
    }

}
