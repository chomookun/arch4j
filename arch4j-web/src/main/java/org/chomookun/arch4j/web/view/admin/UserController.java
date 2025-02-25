package org.chomookun.arch4j.web.view.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.role.AuthorityService;
import org.chomookun.arch4j.core.role.RoleService;
import org.chomookun.arch4j.core.role.model.Authority;
import org.chomookun.arch4j.core.role.model.AuthoritySearch;
import org.chomookun.arch4j.core.role.model.Role;
import org.chomookun.arch4j.core.role.model.RoleSearch;
import org.chomookun.arch4j.core.security.SecurityTokenService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.model.UserSearch;
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
@RequestMapping("admin/users")
@PreAuthorize("hasAuthority('admin.users')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final SecurityTokenService securityTokenService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @GetMapping
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView("admin/users.html");
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

    @PostMapping("save-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.users.edit')")
    public User saveUser(@RequestBody @Valid User user) {
        return userService.saveUser(user);
    }

    @GetMapping("delete-user")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.users.edit')")
    public void deleteUser(@RequestParam("userId") String userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("change-user-password")
    @ResponseBody
    public void changeUserPassword(@RequestBody Map<String,String> payload) {
        userService.changePassword(payload.get("userId"), payload.get("password"));
    }

    @PostMapping(value = "generate-security-token", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.users.edit')")
    public String generateAccessToken(@RequestBody Map<String,String> payload) {
        String userId = Optional.ofNullable(payload.get("userId")).orElseThrow();
        int expirationDays = Optional.ofNullable(payload.get("expirationDays")).map(Integer::parseInt).orElseThrow();
        int expirationMinutes = expirationDays * 60 * 24;
        User user = userService.getUser(userId).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return securityTokenService.encodeSecurityToken(userDetails, expirationMinutes);
    }

}
