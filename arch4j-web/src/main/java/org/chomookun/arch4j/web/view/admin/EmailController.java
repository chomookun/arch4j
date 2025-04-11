package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.email.EmailService;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.email.model.EmailSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/email")
@PreAuthorize("hasAuthority('admin.email')")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/email.html");
    }

    @GetMapping("get-emails")
    @ResponseBody
    public Page<Email> getEmailTemplates(EmailSearch emailSearch, Pageable pageable) {
        return emailService.getEmails(emailSearch, pageable);
    }

    @GetMapping("get-email")
    @ResponseBody
    public Email getEmail(@RequestParam("emailId")String emailId) {
        return emailService.getEmail(emailId)
                .orElseThrow();
    }

    @PostMapping("save-email")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.email:edit')")
    public Email saveEmail(@RequestBody @Valid Email email) {
        return emailService.saveEmail(email);
    }

    @GetMapping("delete-email")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.email:edit')")
    public void deleteEmail(@RequestParam("emailId")String emailId) {
        emailService.deleteEmail(emailId);
    }

}
