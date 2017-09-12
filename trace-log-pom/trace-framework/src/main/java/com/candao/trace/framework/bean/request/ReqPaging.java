package com.candao.trace.framework.bean.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.candao.trace.framework.util.FastStringUtils;

/**
 * 分页请求基类
 */
public class ReqPaging implements Serializable {

	private static final long serialVersionUID = -2300063327371930527L;

	/**
	 * 默认起始页
	 */
	public static final int default_pageNow = 1;

	/**
	 * 默认每页记录数
	 */
	public static final int default_pageSize = 10;

	/**
	 * 最大每页记录数
	 */
	public static final int max_pageSize = 100;

	/**
	 * 起始页(1开始)
	 */
	private int pageNow = default_pageNow;
	/**
	 * 每页记录数
	 */
	private int pageSize = default_pageSize;

	public List<SortRequestParam> sorts = new ArrayList<SortRequestParam>();
	
	/**
	 * 获取排序
	 * @return
	 */
	public Sort getSort(){
		
		Sort sort = null;
		
		if(FastStringUtils.isNotEmpty(sorts)){
			
			// 重复字段排序以最开始为准，后续废弃
			List<SortRequestParam> builderSortRequestParams = new ArrayList<SortRequestParam>();
			
			for(SortRequestParam srp : sorts){
				if(srp != null && FastStringUtils.isNotBlank(srp.field) && !builderSortRequestParams.contains(srp)){
					Sort tempSort = new Sort(srp.asc ? Sort.Direction.ASC : Sort.Direction.DESC, srp.field);
					if(sort == null){
						sort = tempSort;
					} else {
						sort = sort.and(tempSort);
					}
					builderSortRequestParams.add(srp);
				}
			}
		}
		
		return sort;
	}
	
	/**
	 * 新增升序排序字段
	 * @param before
	 * @param field
	 * @return
	 */
	public ReqPaging addSort(boolean before, String field){
		return addSort(before, field, true);
	}
	
	/**
	 * 新增降序排序字段
	 * @param before
	 * @param field
	 * @return
	 */
	public ReqPaging addDescSort(boolean before, String field){
		return addSort(before, field, false);
	}
	
	/**
	 * 新增排序字段
	 * @param before
	 * @param field
	 * @param asc
	 * @return
	 */
	public ReqPaging addSort(boolean before, String field, boolean asc){
		return addSort(before, new SortRequestParam(field, asc));
	}
	
	/**
	 * 新增排序
	 * @param before
	 * @param params
	 * @return
	 */
	public ReqPaging addSort(boolean before, SortRequestParam...params){
		if(params != null){
			for(SortRequestParam s : params){
				if(s != null && !sorts.contains(s)){
					if(before){
						sorts.add(0, s);
					}else{
						sorts.add(s);
					}
				}
			}
		}
		return this;
	}
	
	/**
	 * 获取分页参数
	 * @return
	 */
	public Pageable getPageable() {
		Sort sort = getSort();
		if(sort != null){
			return new PageRequest(getPageNow() - 1, getPageSize(), sort);
		}else{
			return new PageRequest(getPageNow() - 1, getPageSize());
		}
	}

	public int getPageNow() {
		return pageNow < 1 ? 1 : pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		if (pageSize < 0) {
			return 0;
		} else if (pageSize > max_pageSize) {
			return max_pageSize;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<SortRequestParam> getSorts() {
		return sorts;
	}

	public void setSorts(List<SortRequestParam> sorts) {
		this.sorts = sorts;
	}
	
}
