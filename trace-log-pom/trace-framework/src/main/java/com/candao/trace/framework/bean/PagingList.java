package com.candao.trace.framework.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * 分页列表
 * 
 */
public class PagingList<T> implements Serializable {
	
	private static final long serialVersionUID = 2023786763063542457L;

	public List<T> rows = new ArrayList<T>();		//当前分页列表
	
	public int pages;		//总页数
	
	public int total;		//总记录数
	
	public int page;		//当前页数
	
	public int pageSize;		//每页纪录数
	
	
	public PagingList() {
		
	}

	/**
	 * 分页数据转换
	 * @param page
	 * @return
	 */
	public static <T> PagingList<T> getPagingList(Page<T> page){
		
		PagingList<T> r = new PagingList<T>();
		
		if(page != null){
			r.set(page.getNumber() + 1, page.getSize(), page.getContent(), (int) page.getTotalElements());
		}
		
		return r;
	}
	
	
	/**
	 *	设置分页数据
	 * @param page
	 * @param pageSize
	 * @param data
	 * @param total
	 */
	public void set(int page, int pageSize, List<T> data, int total) {
		this.page = page;
		this.pageSize = pageSize;
		this.rows = data;
		this.total = total;
		if (pageSize > 0 && total > 0) {
			int perPage = total / pageSize;
			pages = total % pageSize == 0 ? perPage : perPage + 1;
		}
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
