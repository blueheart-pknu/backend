package org.clubs.blueheart.group.repository;

import org.clubs.blueheart.group.dao.GroupDao;
import org.clubs.blueheart.group.dao.GroupUserDao;
import org.clubs.blueheart.group.domain.Group;
import org.clubs.blueheart.group.domain.GroupUser;
import org.clubs.blueheart.group.dto.request.GroupInfoRequestDto;
import org.clubs.blueheart.group.dto.request.GroupUserRequestDto;
import org.clubs.blueheart.group.dto.response.GroupUserInfoResponseDto;
import org.clubs.blueheart.exception.ExceptionStatus;
import org.clubs.blueheart.exception.RepositoryException;
import org.clubs.blueheart.user.dao.UserDao;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupDao groupDao;
    private final GroupUserDao groupUserDao;
    private final UserDao userDao;

    public GroupRepositoryImpl(GroupDao groupDao, GroupUserDao groupUserDao, UserDao userDao) {
        this.groupDao = groupDao;
        this.groupUserDao = groupUserDao;
        this.userDao = userDao;
    }

    @Override
    public void createGroup(GroupInfoRequestDto groupInfoRequestDto) {
        if (groupInfoRequestDto == null) {
            throw new RepositoryException(ExceptionStatus.AUTH_COOKIE_UNAUTHORIZED);
        }

        boolean groupExists = groupUserDao.existsByUserId(groupInfoRequestDto.getUserId());
        if (groupExists) {
            throw new RepositoryException(ExceptionStatus.GROUP_ALREADY_EXISTS);
        }

        Group group = Group.builder()
                .name(groupInfoRequestDto.getName())
                .build();

        groupDao.save(group);

        User user = userDao.findById(groupInfoRequestDto.getUserId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .build();

        groupUserDao.save(groupUser);
    }

    @Override
    public void deleteGroup(GroupInfoRequestDto groupInfoRequestDto) {
        if (groupInfoRequestDto == null || groupInfoRequestDto.getUserId() == null) {
            throw new RepositoryException(ExceptionStatus.AUTH_INVALID_PARAMS);
        }

        GroupUser groupUser = groupUserDao.findOneByUserIdAndDeletedAtIsNull(groupInfoRequestDto.getUserId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.GROUP_NOT_FOUND));

        List<GroupUser> groupUsers = groupUserDao.findAllByGroupIdAndDeletedAtIsNull(groupUser.getGroup().getId());

        List<GroupUser> updatedGroupUsers = groupUsers.stream()
                .map(gu -> gu.toBuilder()
                        .deletedAt(LocalDateTime.now())
                        .build())
                .toList();

        groupUserDao.saveAll(updatedGroupUsers);

        Group group = groupUser.getGroup();
        Group updatedGroup = group.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        groupDao.save(updatedGroup);
    }

    @Override
    public void addGroupUserById(GroupUserRequestDto groupUserRequestDto) {
        if (groupUserRequestDto == null || groupUserRequestDto.getUserId() == null || groupUserRequestDto.getGroupId() == null) {
            throw new RepositoryException(ExceptionStatus.AUTH_INVALID_PARAMS);
        }

        boolean groupExists = groupUserDao.existsByUserId(groupUserRequestDto.getUserId());
        if (groupExists) {
            throw new RepositoryException(ExceptionStatus.GROUP_ALREADY_EXISTS);
        }

        Group group = groupDao.findById(groupUserRequestDto.getGroupId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.GROUP_NOT_FOUND));
        User user = userDao.findById(groupUserRequestDto.getUserId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        groupUserDao.save(groupUser);
    }

    @Override
    public void removeGroupUserById(GroupUserRequestDto groupUserRequestDto) {
        if (groupUserRequestDto == null || groupUserRequestDto.getUserId() == null || groupUserRequestDto.getGroupId() == null) {
            throw new RepositoryException(ExceptionStatus.AUTH_INVALID_PARAMS);
        }

        GroupUser groupUser = groupUserDao.findByGroupIdAndUserIdAndDeletedAtIsNull(
                groupUserRequestDto.getGroupId(),
                groupUserRequestDto.getUserId()
        ).orElseThrow(() -> new RepositoryException(ExceptionStatus.GROUP_USER_NOT_FOUND));

        GroupUser updatedGroupUser = groupUser.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        groupUserDao.save(updatedGroupUser);
    }

    @Override
    public List<GroupUserInfoResponseDto> getMyGroupInfoById(Long userId) {
        if (userId == null) {
            throw new RepositoryException(ExceptionStatus.AUTH_INVALID_PARAMS);
        }

        GroupUser existGroupUserInfo = groupUserDao.findOneByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.GROUP_NOT_FOUND));

        List<GroupUser> groupUsers = groupUserDao.findAllByGroupIdAndDeletedAtIsNull(existGroupUserInfo.getGroup().getId());

        if (groupUsers.isEmpty()) {
            throw new RepositoryException(ExceptionStatus.GROUP_USER_NOT_FOUND);
        }

        return groupUsers.stream()
                .map(groupUser -> {
                    User user = groupUser.getUser();
                    return GroupUserInfoResponseDto.builder()
                            .id(user.getId())
                            .studentNumber(user.getStudentNumber())
                            .username(user.getUsername())
                            .role(user.getRole())
                            .build();
                })
                .toList();
    }

}