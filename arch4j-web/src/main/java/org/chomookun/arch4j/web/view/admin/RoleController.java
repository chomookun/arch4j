package org.chomookun.arch4j.web.view.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.role.AuthorityService;
import org.chomookun.arch4j.core.role.RoleService;
import org.chomookun.arch4j.core.role.model.Authority;
import org.chomookun.arch4j.core.role.model.AuthoritySearch;
import org.chomookun.arch4j.core.role.model.Role;
import org.chomookun.arch4j.core.role.model.RoleSearch;
import org.chomookun.arch4j.core.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/roles")
@PreAuthorize("hasAuthority('admin.roles')")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    private final AuthorityService authorityService;

    /**
     * Returns roles model and view
     * @return model and view
     */
    @GetMapping
    public ModelAndView roles() {
        ModelAndView modelAndView = new ModelAndView("admin/roles.html");
        modelAndView.addObject("userStatuses", User.Status.values());
        return modelAndView;
    }

    /**
     * Returns page of roles
     * @param roleSearch role search
     * @param pageable pageable
     * @return page of roles
     */
    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

    /**
     * Returns specified role
     * @param roleId role id
     * @return role
     */
    @GetMapping("get-role")
    @ResponseBody
    public Role getRole(@RequestParam("roleId")String roleId) {
        return roleService.getRole(roleId).orElseThrow();
    }

    /**
     * Saves role
     * @param role role
     * @return saved role
     */
    @PostMapping("save-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public Role saveRole(@RequestBody @Valid Role role) {
        return roleService.saveRole(role);
    }

    /**
     * Deletes role
     * @param roleId role id
     */
    @GetMapping("delete-role")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public void deleteRole(@RequestParam("roleId") String roleId) {
        roleService.deleteRole(roleId);
    }

    /**
     * Returns page of authorities
     * @param authoritySearch authority search
     * @param pageable pageable
     * @return page of authorities
     */
    @GetMapping("get-authorities")
    @ResponseBody
    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {
        return authorityService.getAuthorities(authoritySearch, pageable);
    }

    /**
     * Returns specified authority
     * @param authorityId authority id
     * @return authority
     */
    @GetMapping("get-authority")
    @ResponseBody
    public Authority getAuthority(@RequestParam("authorityId") String authorityId) {
        return authorityService.getAuthority(authorityId)
                .orElseThrow();
    }

    /**
     * Saves authority
     * @param authority authority
     * @return saved authority
     */
    @PostMapping("save-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public Authority saveAuthority(@RequestBody @Valid Authority authority) {
        return authorityService.saveAuthority(authority);
    }

    /**
     * Deletes authority
     * @param authorityId authority id
     */
    @GetMapping("delete-authority")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.roles.edit')")
    public void deleteAuthority(@RequestParam("authorityId")String authorityId) {
        authorityService.deleteAuthority(authorityId);
    }

}
