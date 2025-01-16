package org.clubs.blueheart.group.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.clubs.blueheart.group.domain.GroupUser;
import org.clubs.blueheart.group.domain.QGroupUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GroupUserCustomDaoImpl implements GroupUserCustomDao {

    private final JPAQueryFactory queryFactory;

    public GroupUserCustomDaoImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<GroupUser> findOneByUserIdAndDeletedAtIsNull(Long userId) {
        QGroupUser groupUser = QGroupUser.groupUser;

        GroupUser result = queryFactory
                .selectFrom(groupUser)
                .where(groupUser.user.id.eq(userId)
                        .and(groupUser.deletedAt.isNull()))
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}