package org.clubs.blueheart.activity.dao;

import org.clubs.blueheart.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActivityDao extends ActivityCustomDao, JpaRepository<Activity, Long> {
}