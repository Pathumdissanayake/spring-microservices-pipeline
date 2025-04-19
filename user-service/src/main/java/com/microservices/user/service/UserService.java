package com.microservices.user.service;

import com.microservices.user.VO.Department;
import com.microservices.user.VO.ResponseTemplateVO;
import com.microservices.user.entity.User;
import com.microservices.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public User saveUser(User user) {
        log.info("Inside saveUser of UserService");
        return userRepository.save(user);
    }

    public ResponseTemplateVO getUserWithDepartment(Long userId) {
        log.info("Inside getUserWithDepartment of UserService");
        ResponseTemplateVO vo = new ResponseTemplateVO();
        User user = userRepository.findByUserId(userId);

        Department department =
                restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId()
                        ,Department.class);

        vo.setUser(user);
        vo.setDepartment(department);

        return  vo;
    }

    public List<User> getAllUsers() {
        log.info("Inside getAllUsers of UserService");
        return userRepository.findAll();
    }

    public User updateUser(Long userId, User user) {
        log.info("Inside updateUser of UserService");
        User existingUser = userRepository.findByUserId(userId);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setDepartmentId(user.getDepartmentId());
            return userRepository.save(existingUser);
        }
        return null;
    }

    public User deleteUser(Long userId) {
        log.info("Inside deleteUser of UserService");
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            userRepository.deleteById(userId);
            return user;
        }
        return null;
    }
}