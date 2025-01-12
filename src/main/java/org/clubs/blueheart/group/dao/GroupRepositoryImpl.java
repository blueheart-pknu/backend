package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.domain.Group;
import org.clubs.blueheart.group.domain.GroupUser;
import org.clubs.blueheart.group.dto.GroupInfoDto;
import org.clubs.blueheart.group.dto.GroupUserDto;
import org.clubs.blueheart.group.dto.GroupUserInfoDto;
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
    public void createGroup(GroupInfoDto groupInfoDto) {
        if (groupInfoDto == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        boolean groupExists = groupUserDao.existsByUserId(groupInfoDto.getUserId());
        if (groupExists) {
            throw new RepositoryException(ExceptionStatus.GROUP_ALREADY_EXISTS);
        }

        Group group = Group.builder()
                .name(groupInfoDto.getName())
                .build();

        groupDao.save(group);

        User user = userDao.findById(groupInfoDto.getUserId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .build();

        groupUserDao.save(groupUser);
    }

    @Override
    public void deleteGroup(GroupInfoDto groupInfoDto) {
        if (groupInfoDto == null || groupInfoDto.getUserId() == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        GroupUser groupUser = groupUserDao.findOneByUserIdAndDeletedAtIsNull(groupInfoDto.getUserId())
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
    public void addGroupUserById(GroupUserDto groupUserDto) {
        if (groupUserDto == null || groupUserDto.getUserId() == null || groupUserDto.getGroupId() == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        boolean groupExists = groupUserDao.existsByUserId(groupUserDto.getUserId());
        if (groupExists) {
            throw new RepositoryException(ExceptionStatus.GROUP_ALREADY_EXISTS);
        }

        Group group = groupDao.findById(groupUserDto.getGroupId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.GROUP_NOT_FOUND));
        User user = userDao.findById(groupUserDto.getUserId())
                .orElseThrow(() -> new RepositoryException(ExceptionStatus.USER_NOT_FOUND_USER));

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        groupUserDao.save(groupUser);
    }

    @Override
    public void removeGroupUserById(GroupUserDto groupUserDto) {
        if (groupUserDto == null || groupUserDto.getUserId() == null || groupUserDto.getGroupId() == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
        }

        GroupUser groupUser = groupUserDao.findByGroupIdAndUserIdAndDeletedAtIsNull(
                groupUserDto.getGroupId(),
                groupUserDto.getUserId()
        ).orElseThrow(() -> new RepositoryException(ExceptionStatus.GROUP_USER_NOT_FOUND));

        GroupUser updatedGroupUser = groupUser.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        groupUserDao.save(updatedGroupUser);
    }

    @Override
    public List<GroupUserInfoDto> getMyGroupInfoById(Long userId) {
        if (userId == null) {
            throw new RepositoryException(ExceptionStatus.GENERAL_INVALID_ARGUMENT);
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
                    return GroupUserInfoDto.builder()
                            .studentNumber(user.getStudentNumber())
                            .username(user.getUsername())
                            .role(user.getRole())
                            .build();
                })
                .toList();
    }
}