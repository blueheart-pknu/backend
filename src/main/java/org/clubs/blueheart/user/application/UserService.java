package org.clubs.blueheart.user.application;

import org.clubs.blueheart.user.domain.User;
import org.clubs.blueheart.user.dao.UserRepository;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserSearchDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * 사용자 생성
     *
     * @param user 새 사용자 정보
     * @return User
     */
    //TODO: query count 성능 개선: https://jojoldu.tistory.com/516
    public User createUser(UserInfoDto userInfoDto) {
        // 비즈니스 로직 추가 가능 (예: 이메일 유효성 검사)
        return userRepository.createUser(userInfoDto);
    }

    /**
     * 모든 사용자 목록 조회
     *
     * @return List<User>
     */

    public List<UserInfoDto> findUserByKeyword(String keyword) {
        if (isInteger(keyword)) {
            return userRepository.findOneUserByStudentNumber(Integer.parseInt(keyword));
        }
        return userRepository.findOneUserByUsername(keyword);
    }

    private boolean isInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 사용자 업데이트
     *
     * @param id          사용자 ID
     * @param userDetails 수정할 사용자 정보
     * @return ResponseEntity<User>
     */
    public ResponseEntity<User> updateUserById(UserUpdateDto userUpdateDto) {
        return userRepository.updateUserById(userUpdateDto);
//                .map(user -> {
//                    // 기존 사용자 정보 업데이트
//                    user.setName(userDetails.getName());
//                    user.setEmail(userDetails.getEmail());
//                    return ResponseEntity.ok(userRepository.save(user));
//                })
//                .orElse(ResponseEntity.notFound().build());
    }



    /**
     * 사용자 삭제
     *
     * @param id 사용자 ID
     * @return ResponseEntity<Void>
     */
    public ResponseEntity<Void> deleteUserById(UserDeleteDto userDeleteDto) {
        return userRepository.deleteUserById(id);
//                .map(user -> {
//                    userRepository.delete(user);
//                    return ResponseEntity.noContent().build();
//                })
//                .orElse(ResponseEntity.notFound().build());
    }
}