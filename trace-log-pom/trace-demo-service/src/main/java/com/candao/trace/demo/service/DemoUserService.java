package com.candao.trace.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.demo.config.CacheKeys;
import com.candao.trace.demo.repository.DemoUserRepository;
import com.candao.trace.framework.bean.DataStatus;
import com.candao.trace.framework.bean.PagingList;
import com.candao.trace.framework.callback.ISimpleCallback;
import com.candao.trace.framework.data.DataReader;
import com.candao.trace.framework.data.DataWriter;
import com.candao.trace.framework.jpa.specification.Filter;
import com.candao.trace.framework.jpa.specification.QueryParams;
import com.candao.trace.framework.lock.DistributedLock;
import com.candao.trace.framework.service.BaseService;
import com.candao.trace.framework.util.StringUtil;
import com.candao.trace.log.aop.MethodLog;

/**
 * Demo User服务逻辑
 * 
 */
@Component
public class DemoUserService extends BaseService {
	@Autowired
	DemoUserRepository demoUserRepository;
	@Autowired
	DemoSmsService demoSmsService;

	/**
	 * insert对象 示例：<br/>
	 * 新增用户对象
	 * @param user-用户对象
	 * @return
	 */
	public boolean addUser(DemoUser user) {
		user = demoUserRepository.save(user);
		// TODO 是否需要写cache，如不写的话再第一次get时再获取
//		if (user != null) {
//			String cacheKey = CacheKeys.USER_ + user.getId();
//			RedisClient.setValue(cacheKey, user);
//		}
		return user != null;
	}

	/**
	 * db-redis get对象示例：<br/>
	 * 获取指定id的用户
	 * @param uid-用户id
	 * @return
	 */
	@MethodLog
	public DemoUser getUser(int uid) {
		String cacheKey = CacheKeys.USER_ + uid;
		// 调用数据获取封装方法，内部以规范cache与db数据获取流程
//		DemoUser data = DataReader.getDataFromRedisOrDataGeter(cacheKey, DemoUser.class, new IDataGeter<DemoUser>() {
//			@Override
//			public DemoUser getData() {
//				return demoUserRepository.getOne(uid);
//			}
//		});

		DemoUser data = DataReader.getDataFromRedisOrDataGeter(cacheKey, DemoUser.class, () -> {
			return demoUserRepository.getOne(uid);
		});
		return data;

//		return DataReader.getDataFromRedisOrDataGeter(cacheKey, DemoUser.class, () -> {
//			return demoUserRepository.getOne(uid);
//		});
	}

	/**
	 * db-redis update属性示例：<br/>
	 * 更新用户名称
	 * @param uid-用户id
	 * @param name-用户名
	 * @return
	 */
	public boolean upateUserName(int uid, String name) {
		// 分布式锁示例
		String lockKey = "user_" + uid;
		boolean rs = (Boolean)DistributedLock.execute(lockKey, new ISimpleCallback() {
			@Override
			public Object execute() {
				String cacheKey = CacheKeys.USER_ + uid;
				boolean rs = DataWriter.updateDataAndDelRedisKey(cacheKey, () -> {
					return demoUserRepository.updateUserName(uid, name) > 0;
				});
				return rs;
			}
		});
		return rs;
	}

	/**
	 * db-redis 批量update属性示例：<br/>
	 * 批量更新状态
	 * @param uids-用户id集合
	 * @param status-状态
	 * @return
	 */
	public boolean upateStatus(List<Integer> uids, int status) {
		List<String> cacheKeys = new ArrayList<String>();
		uids.forEach(id -> cacheKeys.add(CacheKeys.USER_ + id));
		boolean rs = DataWriter.batchUpdateDataAndDelRedisKey(cacheKeys, () -> {
			return demoUserRepository.updateStatus(uids, status) > 0;
		});
		return rs;
	}

	/**
	 * db-redis update对象示例<br/>
	 * 更新User对象
	 * @param user-最新的user对象
	 * @return
	 */
	public boolean updateUser(int uid, String name, int status) {
		String cacheKey = CacheKeys.USER_ + uid;
		DemoUser rs = DataWriter.updateDataAndSynRedis(cacheKey, () -> {
			// 1.先获取最新的对象
			DemoUser demoUser = getUser(uid);
			// 2.再赋值
			demoUser.setName(name);
			demoUser.setStatus(status);
			// 3.再更新db
			return demoUserRepository.update(demoUser);
		});
		return rs != null;
	}
	
	public void deleteXX(int id) {
		demoUserRepository.delete(id);
	}

	/**
	 * db 固定参数获取List示例<br/>
	 * 获取name like 指定值， status!=删除状态的用户列表，并且按照id降序排列
	 * @param name-搜索名称字符串
	 * @param status-状态
	 * @return
	 */
	public List<DemoUser> getUserList(String name, int status) {
		// 1. sql 前缀
		StringBuffer sql = new StringBuffer("select * from demo_user");
		// 2. 构建where条件 where 1=1 and id =:uid and status =:status
		QueryParams<DemoUser> queryParams = new QueryParams<DemoUser>();
		queryParams.and(Filter.like("name", name), Filter.ne("status", DataStatus.DELETE.getValue()));
		// 3. 指定排序字段
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		// 4. 执行sql
		List<DemoUser> list = demoUserRepository.findByQueryParam(sql, queryParams, sort);
		return list;
	}
	
	/**
	 * db 动态参数获取List示例<br/>
	 * 获取name like 指定值， status!=删除状态的用户列表，并且按照id降序排列
	 * @param name-搜索名称字符串
	 * @param status-状态
	 * @return
	 */
	public List<DemoUser> getUserList2(String name, int status) {
		// 1. sql 前缀
		StringBuffer sql = new StringBuffer("select * from demo_user");
		// 2. 构建where条件 where 1=1 and id =:uid and status =:status
		QueryParams<DemoUser> queryParams = new QueryParams<DemoUser>();
		if (StringUtil.isNullOrEmpty(name)) {// 判断是否需要拼接到sql语句
			queryParams.and(Filter.like("name", name));
		}
		queryParams.and(Filter.ne("status", DataStatus.DELETE.getValue()));
		// 3. 指定排序字段
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		// 4. 执行sql
		List<DemoUser> list = demoUserRepository.findByQueryParam(sql, queryParams, sort);
		return list;
	}

	/**
	 * db 固定参数获取PagingList示例：<br/>
	 * 获取id>指定值，status！=删除状态的用户列表，并且按照id降序排列，用pageNow和pageSize分页
	 * @param pageNow-当前页数
	 * @param pageSize-每页获取数
	 * @param uid-用户id
	 * @param status-状态
	 * @return
	 */
	public PagingList<DemoUser> getUserPagingList(int pageNow, int pageSize, int uid, int status) {
		// 1. sql 前缀
//		String sql = "select * from demo_user";
//		String countSql = "select count(*) from demo_user";
		// 2. 构建where条件 where 1=1 and id =:uid and status =:status
		QueryParams<DemoUser> queryParams = new QueryParams<DemoUser>();
		queryParams.and(Filter.gt("id", uid), Filter.ne("status", DataStatus.DELETE.getValue()));
		// 3. 指定排序字段
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		PagingList<DemoUser> pagingList = getPagingList(demoUserRepository, pageNow, pageSize, queryParams, sort);
		return pagingList;
	}
	
	/**
	 * db 动态参数获取PagingList示例：<br/>
	 * 获取id>指定值，status！=删除状态的用户列表，并且按照id降序排列，用pageNow和pageSize分页
	 * @param pageNow-当前页数
	 * @param pageSize-每页获取数
	 * @param uid-用户id
	 * @param status-状态
	 * @return
	 */
	public PagingList<DemoUser> getUserPagingList2(int pageNow, int pageSize, int uid, int status, String name) {
		// 1. sql 前缀
//		String sql = "select * from demo_user";
//		String countSql = "select count(*) from demo_user";
		// 2. 构建where条件 where 1=1 and id =:uid and status =:status
		QueryParams<DemoUser> queryParams = new QueryParams<DemoUser>();
		queryParams.and(Filter.gt("id", uid), Filter.ne("status", DataStatus.DELETE.getValue()));
		if (StringUtil.isNullOrEmpty(name)) {// 判断是否需要拼接到sql语句
			queryParams.and(Filter.like("name", name));
		}
		// 3. 指定排序字段
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		PagingList<DemoUser> pagingList = getPagingList(demoUserRepository, pageNow, pageSize, queryParams, sort);
		return pagingList;
	}

	/**
	 * 添加用户发送确认短信
	 * @param user
	 */
	public void sendConfiringSms(DemoUser user) {
		demoSmsService.sendSms(user);
	}

	public static void main(String[] args) {
		List<Integer> uids = new ArrayList<Integer>();
		uids.add(1);
		uids.add(2);
		uids.add(3);
		List<String> cacheKeys = new ArrayList<String>();
		uids.forEach(id -> cacheKeys.add(CacheKeys.USER_ + id));
		for (String s : cacheKeys) {
			System.out.println(s);
		}
	}
}
