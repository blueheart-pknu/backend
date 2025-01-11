package org.clubs.blueheart.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.clubs.blueheart.exception.CustomExceptionStatus;
import org.clubs.blueheart.response.GlobalResponseHandler;
import org.clubs.blueheart.response.ResponseStatus;
import org.clubs.blueheart.user.application.UserService;
import org.clubs.blueheart.user.dto.UserDeleteDto;
import org.clubs.blueheart.user.dto.UserInfoDto;
import org.clubs.blueheart.user.dto.UserUpdateDto;
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
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 등록됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾지 못함",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<GlobalResponseHandler<Void>> createUser(@RequestBody @Valid UserInfoDto userInfoDto) {
        userService.createUser(userInfoDto);
        return GlobalResponseHandler.success(ResponseStatus.USER_CREATED);
    }

    @Operation(summary = "사용자 검색", description = "사용자 정보를 검색하는 API, keyword가 없을 시 모두 반환")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 검색 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾지 못함",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)))
    })
    @GetMapping("/search/{keyword}")
    public ResponseEntity<GlobalResponseHandler<List<UserInfoDto>>> findUserByKeyword(@PathVariable String keyword) {
        List<UserInfoDto> users = userService.findUserByKeyword(keyword);
        return GlobalResponseHandler.success(ResponseStatus.USER_SEARCHED, users);
    }

    @Operation(summary = "사용자 정보 업데이트", description = "특정 사용자의 정보를 업데이트 하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 업데이트 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾지 못함",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<GlobalResponseHandler<Void>> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        userService.updateUserById(userUpdateDto);
        return GlobalResponseHandler.success(ResponseStatus.USER_UPDATED);
    }

    @Operation(summary = "사용자 정보 삭제", description = "특정 사용자의 정보를 삭제 하는 API 입니다. (Soft Delete)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStatus.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾지 못함",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomExceptionStatus.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<GlobalResponseHandler<Void>> deleteUser(@RequestBody @Valid UserDeleteDto userDeleteDto) {
        userService.deleteUserById(userDeleteDto);
        return GlobalResponseHandler.success(ResponseStatus.USER_DELETED);
    }
}