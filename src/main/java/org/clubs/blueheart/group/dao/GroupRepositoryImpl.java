package org.clubs.blueheart.group.dao;

import org.clubs.blueheart.group.domain.Group;
import org.clubs.blueheart.group.domain.GroupUser;
import org.clubs.blueheart.group.dto.GroupInfoDto;
import org.clubs.blueheart.group.dto.GroupUserDto;
import org.clubs.blueheart.group.dto.GroupUserInfoDto;
import org.clubs.blueheart.user.dao.UserDao;
import org.clubs.blueheart.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class GroupRepositoryImpl implements  GroupRepository{

    private GroupDao groupDao;
    private GroupUserDao groupUserDao;
    private UserDao userDao;

    public GroupRepositoryImpl(GroupDao groupDao, GroupUserDao groupUserDao, UserDao userDao) {
        this.groupDao = groupDao;
        this.groupUserDao = groupUserDao;
        this.userDao = userDao;
    }

    @Override
    public void createGroup(GroupInfoDto groupInfoDto) {
        if (groupInfoDto == null) {
            throw new IllegalArgumentException("groupInfoDto can not be null");
        }

        boolean groupExists = groupUserDao.existsByUserId(groupInfoDto.getUserId());
        if (groupExists) {
            throw new IllegalArgumentException("group already exists");
        }

        Group group = Group.builder()
                .name(groupInfoDto.getName())
                .build();

        groupDao.save(group);

        User user = userDao.findById(groupInfoDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + groupInfoDto.getUserId()));

        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(user)
                .build();

        groupUserDao.save(groupUser);

    }

    //TODO: Query로 최적화
    @Override
    public void deleteGroup(GroupInfoDto groupInfoDto) {
        if (groupInfoDto == null || groupInfoDto.getUserId() == null) {
            throw new IllegalArgumentException("GroupInfoDto or userId cannot be null");
        }

        // Fetch the GroupUser entry for the user (assuming a user can only delete a group they belong to)
        GroupUser groupUser = groupUserDao.findOneByUserIdAndDeletedAtIsNull(groupInfoDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No active group found for userId: " + groupInfoDto.getUserId()));

        // Fetch all GroupUser relationships for the group
        List<GroupUser> groupUsers = groupUserDao.findAllByGroupIdAndDeletedAtIsNull(groupUser.getGroup().getId());

        // Mark all GroupUser relationships as deleted
        List<GroupUser> updatedGroupUsers = groupUsers.stream()
                .map(gu -> gu.toBuilder()
                        .deletedAt(LocalDateTime.now())
                        .build())
                .toList();

        // Save all updated GroupUser entities
        groupUserDao.saveAll(updatedGroupUsers);

        // Mark the group itself as deleted
        Group group = groupUser.getGroup();
        Group updatedGroup = group.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        // Save the updated group
        groupDao.save(updatedGroup);
    }

    @Override
    public void addGroupUserById(GroupUserDto groupUserDto) {
        if (groupUserDto == null || groupUserDto.getUserId() == null || groupUserDto.getGroupId() == null) {
            throw new IllegalArgumentException("GroupUserDto or its fields cannot be null");
        }

        // Check if the user is already part of the group
        boolean groupExists = groupUserDao.existsByUserId(groupUserDto.getUserId());
        if (groupExists) {
            throw new IllegalArgumentException("group already exists");
        }

        // Fetch the group and user
        Group group = groupDao.findById(groupUserDto.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + groupUserDto.getGroupId()));
        User user = userDao.findById(groupUserDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + groupUserDto.getUserId()));

        // Add the user to the group
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
            throw new IllegalArgumentException("GroupUserDto or its fields cannot be null");
        }

        // Fetch the GroupUser relationship
        GroupUser groupUser = groupUserDao.findByGroupIdAndUserIdAndDeletedAtIsNull(
                groupUserDto.getGroupId(),
                groupUserDto.getUserId()
        ).orElseThrow(() -> new IllegalArgumentException("User is not part of the group"));

        // Mark the relationship as deleted (Soft delete)
        GroupUser updatedGroupUser = groupUser.toBuilder()
                .deletedAt(LocalDateTime.now())
                .build();

        groupUserDao.save(updatedGroupUser);
    }

    //TODO: Query로 최적화
    @Override
    public List<GroupUserInfoDto> getMyGroupInfoById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Fetch the GroupUser entry for the user (assuming a user can only delete a group they belong to)
        GroupUser existGroupUserInfo = groupUserDao.findOneByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("No active group found for userId: " + userId));

        // Fetch all GroupUser relationships for the group
        List<GroupUser> groupUsers = groupUserDao.findAllByGroupIdAndDeletedAtIsNull(existGroupUserInfo.getGroup().getId());

        if (groupUsers.isEmpty()) {
            throw new IllegalStateException("No active groups found for userId: " + userId);
        }

        // Map GroupUser entities to GroupUserInfoDto
        return groupUsers.stream()
                .map(groupUser -> {
                    User user = groupUser.getUser(); // Fetch User entity from GroupUser
                    return GroupUserInfoDto.builder()
                            .studentNumber(user.getStudentNumber()) // Assuming studentNumber exists in User
                            .username(user.getUsername())
                            .role(user.getRole())
                            .build();
                })
                .toList();
    }


}
