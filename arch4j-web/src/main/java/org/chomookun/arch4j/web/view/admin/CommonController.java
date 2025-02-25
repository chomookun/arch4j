package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.role.model.Role;
import org.chomookun.arch4j.core.role.model.RoleSearch;
import org.chomookun.arch4j.core.role.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/common")
@PreAuthorize("hasAuthority('admin.common')")
@RequiredArgsConstructor
public class CommonController {

    private final RoleService roleService;

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

}
