package org.chomookun.arch4j.web.view.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.SecurityProperties;
import org.chomookun.arch4j.core.security.SecurityTokenService;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.security.model.SecurityToken;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.model.UserSearch;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/user")
@PreAuthorize("hasAuthority('admin.user')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final SecurityProperties securityProperties;

    private final SecurityTokenService securityTokenService;

    private final TotpService totpService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/user");
        modelAndView.addObject("userStatuses", User.Status.values());
        return modelAndView;
    }

    @GetMapping("get-users")
    @ResponseBody
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        return userService.getUsers(userSearch, pageable);
    }

    @GetMapping("get-user")
    @ResponseBody
    public User getUser(@RequestParam("userId") String userId) {
        return userService.getUser(userId).orElseThrow();
    }

    @GetMapping("get-user-by-username")
    @ResponseBody
    public User getUserByUsername(@RequestParam("username") String username) {
        return userService.getUserByUsername(username).orElseThrow();
    }

    @PostMapping("save-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.user:edit')")
    public User saveUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    @GetMapping("delete-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.user:edit')")
    public void deleteUser(@RequestParam("userId") String userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("change-user-password")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.user:edit')")
    public void changeUserPassword(@RequestBody Map<String,String> payload) {
        String userId = Optional.ofNullable(payload.get("userId"))
                .orElseThrow(IllegalArgumentException::new);
        String password = Optional.ofNullable(payload.get("password"))
                .orElseThrow(IllegalArgumentException::new);
        userService.changePassword(userId, password);
    }

    @PostMapping(value = "generate-security-token", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.user:edit')")
    public String generateAccessToken(@RequestBody Map<String,String> payload) {
        String userId = Optional.ofNullable(payload.get("userId"))
                .orElseThrow(IllegalArgumentException::new);
        int expirationDays = Optional.ofNullable(payload.get("expirationDays")).map(Integer::parseInt)
                .orElseThrow(IllegalArgumentException::new);
        int expirationMinutes = expirationDays * 60 * 24;
        User user = userService.getUser(userId).orElseThrow();
        SecurityToken securityToken = SecurityToken.builder()
                .userId(user.getUserId())
                .build();
        return securityTokenService.encodeSecurityToken(securityToken, expirationMinutes);
    }

    @PostMapping(value = "generate-totp-qr-code", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.user:edit')")
    public String generateTotpQrCode(@RequestBody Map<String,String> payload) {
        String userId = Optional.ofNullable(payload.get("userId"))
                .orElseThrow(IllegalArgumentException::new);
        User user = userService.getUser(userId).orElseThrow();

        // if TOTP secret is not set, create a new one
        String totpSecret = user.getTotpSecret();
        if (totpSecret == null) {
            totpSecret = userService.createTotpSecret(userId);
        }

        String issuer = securityProperties.getIssuer();
        String account = user.getUsername();
        return totpService.generateTotpQrCode(totpSecret, issuer, account);
    }

}
