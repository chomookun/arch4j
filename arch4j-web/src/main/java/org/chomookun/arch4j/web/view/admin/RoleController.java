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
@RequestMapping("admin/roles")
@PreAuthorize("hasAuthority('admin.roles')")
@RequiredArgsConstructor
public class RoleController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final SecurityTokenService securityTokenService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @GetMapping
    public ModelAndView roles() {
        ModelAndView modelAndView = new ModelAndView("admin/roles.html");
        modelAndView.addObject("userStatuses", User.Status.values());
        return modelAndView;
    }

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

    @GetMapping("get-role")
    @ResponseBody
    public Role getRole(@RequestParam("roleId")String roleId) {
        return roleService.getRole(roleId)
                .orElseThrow();
    }

    @PostMapping("save-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public Role saveRole(@RequestBody @Valid Role role) {
        return roleService.saveRole(role);
    }

    @GetMapping("delete-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public void deleteRole(@RequestParam("roleId") String roleId) {
        roleService.deleteRole(roleId);
    }

    @GetMapping("get-authorities")
    @ResponseBody
    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {
        return authorityService.getAuthorities(authoritySearch, pageable);
    }

    @GetMapping("get-authority")
    @ResponseBody
    public Authority getAuthority(@RequestParam("authorityId") String authorityId) {
        return authorityService.getAuthority(authorityId)
                .orElseThrow();
    }

    @PostMapping("save-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public Authority saveAuthority(@RequestBody @Valid Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    @GetMapping("delete-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public void deleteAuthority(@RequestParam("authorityId")String authorityId) {
        authorityService.deleteAuthority(authorityId);
    }

}
