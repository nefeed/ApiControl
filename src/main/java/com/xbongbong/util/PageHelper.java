package com.xbongbong.util;

import java.util.ArrayList;
import java.util.List;


public class PageHelper {
	
	public static final Integer	PAGE_SIZE		= 20;
	
	public static final Integer	INTERVAL		= 4;
	
	private Integer				pageSize		= PAGE_SIZE;				//每页记录数
																			
	private Integer				rowsCount;									//总记录数
																			
	private Integer				currentPageNum	= 1;						//当前页数
																			
	private Integer				pageTotal;									//总页数
																			
	private List<Integer>		leftPageNums	= new ArrayList<Integer>(); //左页码数
																			
	private List<Integer>		rightPageNums	= new ArrayList<Integer>(); //右页码数
																			
	private Boolean				hasLeft			= false;					//是否有上一页
																			
	private Boolean				hasRight		= false;					//是否有下一页
		
	private String 				pageUriStr		= null; //翻页的地址
	/**
	 * 初始化分页工具
	 * 构建一个<code>PageHelper.java</code>
	 * @param currentPage
	 */
	public PageHelper(Integer currentPage) {
		this.currentPageNum = currentPage;
	}
	
	/**
	 * 初始化分页工具
	 * 构建一个<code>PageHelper.java</code>
	 * @param currentPage
	 * @param pageSize
	 */
	public PageHelper(Integer currentPage, Integer pageSize) {
		this.currentPageNum = currentPage;
		this.pageSize = pageSize;
	}
	
	private void processLeft() {
		if (this.currentPageNum <= INTERVAL) {
			this.leftPageNums = setPageNums(1, this.currentPageNum - 1);
		} else {
			this.leftPageNums = setPageNums(this.currentPageNum - INTERVAL, this.currentPageNum - 1);
		}
	}
	
	private void processRight() {
		if (this.currentPageNum + INTERVAL < this.pageTotal) {
			this.rightPageNums = setPageNums(this.currentPageNum + 1, this.currentPageNum
																		+ INTERVAL);
		} else {
			this.rightPageNums = setPageNums(this.currentPageNum + 1, this.pageTotal);
		}
	}
	
	/**
	 * 设置 页码队列
	 * @param fromPageNum 开始页数
	 * @param toPageNum 结束页数
	 * @return
	 */
	private List<Integer> setPageNums(int fromPageNum, int toPageNum) {
		List<Integer> pages = new ArrayList<Integer>();
		if (toPageNum >= fromPageNum) {
			for (int pageNum = fromPageNum; pageNum <= toPageNum; pageNum++) {
				pages.add(pageNum);
			}
		}
		return pages;
	}
	
	private void initPageHelper() {
		if (this.rowsCount % this.pageSize == 0) {
			this.pageTotal = this.rowsCount / this.pageSize;
		} else {
			this.pageTotal = this.rowsCount / this.pageSize + 1;
		}
		
		this.pageTotal = this.pageTotal <= 0 ? 1 : this.pageTotal;
		 
		if (this.currentPageNum.equals(1) && !this.currentPageNum.equals( this.pageTotal)) {
			this.hasLeft = false;
			this.hasRight = true;
		} else if (this.currentPageNum == 1 && this.currentPageNum.equals( this.pageTotal)) {
			this.hasLeft = false;
			this.hasRight = false;
		} else if (this.currentPageNum != 1 && this.currentPageNum.equals( this.pageTotal)) {
			this.hasLeft = true;
			this.hasRight = false;
		} else {
			this.hasLeft = true;
			this.hasRight = true;
		}
		
		processLeft();
		processRight();
		
	}
	
	public void setRowsCount(Integer rowsCount) {
		this.rowsCount = rowsCount;
		initPageHelper();//初始化
	}
	
	public Integer getCurrentPageNum() {
		return currentPageNum;
	}
	
	public void setCurrentPageNum(Integer currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
	
	public Integer getPageTotal() {
		return pageTotal;
	}
	
	public void setPageTotal(Integer pageTotal) {
		this.pageTotal = pageTotal;
	}
	
	public List<Integer> getLeftPageNums() {
		return leftPageNums;
	}
	
	public void setLeftPageNums(List<Integer> leftPageNums) {
		this.leftPageNums = leftPageNums;
	}
	
	public List<Integer> getRightPageNums() {
		return rightPageNums;
	}
	
	public void setRightPageNums(List<Integer> rightPageNums) {
		this.rightPageNums = rightPageNums;
	}
	
	public Integer getRowsCount() {
		return rowsCount;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Boolean getHasLeft() {
		return hasLeft;
	}
	
	public void setHasLeft(Boolean hasLeft) {
		this.hasLeft = hasLeft;
	}
	
	public Boolean getHasRight() {
		return hasRight;
	}
	
	public void setHasRight(Boolean hasRight) {
		this.hasRight = hasRight;
	}
	
	public String getPageUriStr() {
		return pageUriStr;
	}

	public void setPageUriStr(String pageUriStr) {
		this.pageUriStr = pageUriStr;
	}

	@Override
	public String toString() {
		return String
			.format(
				"PageHelper [pageSize=%s, rowsCount=%s, currentPageNum=%s, pageTotal=%s, leftPageNums=%s, rightPageNums=%s, hasLeft=%s, hasRight=%s]",
				pageSize, rowsCount, currentPageNum, pageTotal, leftPageNums, rightPageNums,
				hasLeft, hasRight);
	}
	
}
