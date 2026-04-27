package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.springsecurity.requests.DemoResourceRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo/abac")
@Tag(name = "DEMO ABAC", description = "DEMO RBAC + ABAC")
public class DemoAbacController {

    /**
     * RBAC:
     * - User phải có quyền USER_VIEW
     * <p>
     * ABAC:
     * - Nếu USER_VIEW có policy thì sẽ check policy
     * - Nếu USER_VIEW không có policy thì pass vì RBAC đã pass
     */
    @GetMapping("/users/view")
    @PreAuthorize("@authz.canAccess(authentication, 'USER_VIEW')")
    public String viewUser() {
        return "Allow view user";
    }

    /**
     * RBAC:
     * - User phải có quyền USER_EDIT
     * <p>
     * ABAC ví dụ:
     * - Chỉ được sửa resource của chính mình
     * - user.userId == request.ownerUserId
     */
    @PutMapping("/users/edit-own")
    @PreAuthorize("@authz.canAccess(authentication, 'USER_EDIT', #request)")
    public String editOwnUser(@RequestBody DemoResourceRequest request) {
        return "Allow edit own user: " + request.getOwnerUserId();
    }

    /**
     * RBAC:
     * - User phải có quyền USER_DELETE
     * <p>
     * ABAC ví dụ:
     * - Chỉ được xóa resource cùng chi nhánh
     * - user.branchCode == request.branchCode
     */
    @DeleteMapping("/users/delete-by-branch")
    @PreAuthorize("@authz.canAccess(authentication, 'USER_DELETE', #request)")
    public String deleteUserByBranch(@RequestBody DemoResourceRequest request) {
        return "Allow delete user in branch: " + request.getBranchCode();
    }

    /**
     * RBAC:
     * - User phải có quyền USER_EDIT
     * <p>
     * ABAC ví dụ:
     * - Chỉ được sửa resource cùng phòng ban
     * - user.departmentCode == request.departmentCode
     */
    @PutMapping("/users/edit-by-department")
    @PreAuthorize("@authz.canAccess(authentication, 'USER_EDIT', #request)")
    public String editUserByDepartment(@RequestBody DemoResourceRequest request) {
        return "Allow edit user in department: " + request.getDepartmentCode();
    }
}