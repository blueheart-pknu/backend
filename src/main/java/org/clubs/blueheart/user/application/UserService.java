package org.clubs.blueheart.user.application;

import org.clubs.blueheart.user.dao.UserRepository;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.clubs.blueheart.user.util.UserValidationUtil.isInteger;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //TODO: query count 성능 개선: https://jojoldu.tistory.com/516
    public void createUser(UserInfoDto userInfoDto) {
        // 비즈니스 로직 추가 가능 (예: 이메일 유효성 검사)
        userRepository.createUser(userInfoDto);
    }


    public List<UserInfoDto> findUserByKeyword(String keyword) {
        if (isInteger(keyword)) {
            return userRepository.findUserByStudentNumber(keyword);
        }
        return userRepository.findUserByUsername(keyword);
    }


    public void updateUserById(UserUpdateDto userUpdateDto) {
        userRepository.updateUserById(userUpdateDto);
    }


    public void deleteUserById(UserDeleteDto userDeleteDto) {
        userRepository.deleteUserById(userDeleteDto);
    }


    public List<UserInfoDto> findAllUser() {
        return userRepository.findAllUser();
    }
}

