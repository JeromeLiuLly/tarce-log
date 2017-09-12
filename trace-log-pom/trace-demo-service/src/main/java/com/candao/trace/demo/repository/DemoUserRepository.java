package com.candao.trace.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.framework.jpa.repository.base.BaseRepository;

/**
 * DemoUser DB访问层
 * 
 */
public interface DemoUserRepository extends BaseRepository<DemoUser, Integer> {

	@Modifying
	@Transactional
	@Query(value = "update demo_user set name = :name where id = :id", nativeQuery = true)
	int updateUserName(@Param(value = "id") Integer id, @Param(value = "name") String name);

	@Modifying
	@Transactional
	@Query(value = "update demo_user set status = :status where id in :ids", nativeQuery = true)
	int updateStatus(@Param(value = "ids") List<Integer> ids, @Param(value = "status") Integer status);

	@Transactional
	@Query(value = "select * from demo_user where name like :name and status = :status", nativeQuery = true)
	List<DemoUser> getUserList(@Param(value = "name") String name, @Param(value = "status") int status);
}
