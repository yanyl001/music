package com.demo.music.util;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 * 
 * @author yanyl
 * @date 2019-04-09
 */
public class PageUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 总记录数 */
	private long totalCount;
	/** 每页记录数 */
	private long pageSize;
	//总页数
	/** 总记录数 */
	private long totalPage;
	/** 当前页数 */
	private long currPage;
	/** 列表数据 */
	private List<?> list;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtil(List<?> list, Long totalCount, Long pageSize, Long currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
}
