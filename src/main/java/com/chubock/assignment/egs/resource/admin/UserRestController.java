package com.chubock.assignment.egs.resource.admin;

import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.service.BaseService;
import com.chubock.assignment.egs.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class UserRestController extends AbstractRestController<UserModel> {
    
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Override
    BaseService<UserModel> getService() {
        return userService;
    }
}
