package coderead.test.dubbo.service;

/**
 * @author tommy
 * @title: UserServiceImpl
 * @projectName dubbo-test
 * @description: TODO
 * @date 2020/4/109:44 AM
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Integer id) {
        User user=new User();
        user.setId(id);
        user.setName("鲁班大叔");
        return user;
    }
}
