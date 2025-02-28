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
@RequestMapping("admin/emails")
@PreAuthorize("hasAuthority('admin.emails')")
@RequiredArgsConstructor
public class EmailsController {

    private final EmailService emailService;

    /**
     * Emails page
     * @return model and view
     */
    @GetMapping
    public ModelAndView emails() {
        return new ModelAndView("admin/emails.html");
    }

    /**
     * Gets email
     * @param emailSearch email search
     * @param pageable pageable
     * @return page of emails
     */
    @GetMapping("get-emails")
    @ResponseBody
    public Page<Email> getEmailTemplates(EmailSearch emailSearch, Pageable pageable) {
        return emailService.getEmails(emailSearch, pageable);
    }

    /**
     * Gets email
     * @param emailId email id
     * @return email
     */
    @GetMapping("get-email")
    @ResponseBody
    public Email getEmail(@RequestParam("emailId")String emailId) {
        return emailService.getEmail(emailId)
                .orElseThrow();
    }

    /**
     * Saves email
     * @param email email
     * @return saved email
     */
    @PostMapping("save-email")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.emails.edit')")
    public Email saveEmail(@RequestBody @Valid Email email) {
        return emailService.saveEmail(email);
    }

    /**
     * Deletes email
     * @param emailId email id
     */
    @GetMapping("delete-email")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.emails.edit')")
    public void deleteEmail(@RequestParam("emailId")String emailId) {
        emailService.deleteEmail(emailId);
    }

}
