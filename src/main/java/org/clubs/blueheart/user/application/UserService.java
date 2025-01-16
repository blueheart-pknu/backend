package org.clubs.blueheart.user.application;

import org.clubs.blueheart.user.dao.UserRepository;
import org.clubs.blueheart.user.dto.request.UserDeleteRequestDto;
import org.clubs.blueheart.user.dto.request.UserInfoRequestDto;
import org.clubs.blueheart.user.dto.request.UserUpdateRequestDto;
import org.clubs.blueheart.user.dto.response.UserInfoResponseDto;
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
    public void createUser(UserInfoRequestDto userInfoRequestDto) {
        // 비즈니스 로직 추가 가능 (예: 이메일 유효성 검사)
        userRepository.createUser(userInfoRequestDto);
    }


    public List<UserInfoResponseDto> findUserByKeyword(String keyword) {
        if (isInteger(keyword)) {
            return userRepository.findUserByStudentNumber(keyword);
        }
        return userRepository.findUserByUsername(keyword);
    }


    public void updateUserById(UserUpdateRequestDto userUpdateRequestDto) {
        userRepository.updateUserById(userUpdateRequestDto);
    }


    public void deleteUserById(UserDeleteRequestDto userDeleteRequestDto) {
        userRepository.deleteUserById(userDeleteRequestDto);
    }


    public List<UserInfoResponseDto> findAllUser() {
        return userRepository.findAllUser();
    }
}

