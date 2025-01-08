package org.clubs.blueheart.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.clubs.blueheart.user.application.UserService;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "사용자 생성", description = "단일의 사용자를 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 등록됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode= "404", description = "사용자를 찾지 못함")
    })
    @Parameters({
            @Parameter(name = "username", description = "사용자이름",
                    example = "홍길동"),
            @Parameter(name = "studentNumber", description = "고유학번", example = "201234567"),
            @Parameter(name = "role", description = "직책" , example = "USER")
    })
    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody @Valid UserInfoDto userInfoDto) {
        userService.createUser(userInfoDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "사용자 검색", description = "사용자 정보를 검색하는 API, keyword 가 없을 시 모두 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 검색 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾지 못함"),
    })
    @Parameters({
            @Parameter(name = "keyword", description = "사용자이름 혹은 고유학번",
                    example = "201234567"),
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserInfoDto>> findUserByKeyword(@PathVariable String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserByKeyword(keyword));
    }

//    @Operation(summary = "사용자 정보 업데이트", description = "특정 사용자의 정보를 업데이트 하는 API 입니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "사용자 정보 업데이트 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//            @ApiResponse(responseCode= "404", description = "사용자를 찾지 못함"),
//    })
//    @Parameters({
//            @Parameter(name = "id", description = "사용자ID",
//                    example = "1"),
//            @Parameter(name = "username", description = "사용자이름, nullable",
//                    example = "홍길동"),
//            @Parameter(name = "studentNumber", description = "사용자 학번, nullable",
//                    example = "201234567"),
//            @Parameter(name = "role", description = "사용자 역할", example = "USER")
//    })
//    @PutMapping("/update")
//    public ResponseEntity updateUser(@RequestBody UserUpdateDto userUpdateDto) {
//        userService.updateUserById(userUpdateDto);
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    @Operation(summary = "사용자 정보 삭제", description = "특정 사용자의 정보를 삭제 하는 API 입니다.(Soft Delete)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "사용자 정보 삭제 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//            @ApiResponse(responseCode= "404", description = "사용자를 찾지 못함"),
//    })
//    @Parameters({
//            @Parameter(name = "id", description = "사용자ID",
//                    example = "1"),
//    })
//    @DeleteMapping("/delete")
//    public ResponseEntity deleteUser(@PathVariable UserDeleteDto userDeleteDto) {
//        userService.deleteUserById(userDeleteDto);
//        return new ResponseEntity(HttpStatus.OK);
//    }
}