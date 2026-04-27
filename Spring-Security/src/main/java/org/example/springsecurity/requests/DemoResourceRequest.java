package org.example.springsecurity.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoResourceRequest {
    /**
     * Chủ sở hữu resource.
     * Dùng cho rule:
     * user.userId == request.ownerUserId
     */
    private String ownerUserId;

    /**
     * Chi nhánh của resource.
     * Dùng cho rule:
     * user.branchCode == request.branchCode
     */
    private String branchCode;

    /**
     * Phòng ban của resource.
     * Dùng cho rule:
     * user.departmentCode == request.departmentCode
     */
    private String departmentCode;

    private String content;
}
