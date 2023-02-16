package com.iwi.iwms.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Pagination {
	
	@Schema(description = "페이지 당 보여줄 데이터 개수")
	private int limitPerPage;
	
	@Schema(description = "블럭 당 보여줄 페이지 개수")
	private int pagePerBlock;
	
	@Schema(description = "전체 데이터 개수")
	private long totalCount;
	
	@Schema(description = "현재 페이지")
	private int currPage;
	
	@Schema(description = "현재 블럭")
	private int currBlock;
	
	@Schema(description = "전체 페이지 개수")
	private int totalPage;
	
	@Schema(description = "전체 블럭 개수")
	private int totalBlock;
	
	@Schema(description = "DB OFFSET")
	private int offset;
	
	@Builder
	public Pagination(int currPage, int limitPerPage, long totalCount) {
		try {
			this.limitPerPage = limitPerPage;
			this.pagePerBlock = 10;
			this.currPage = currPage;
			this.currBlock = (int) Math.ceil((double) currPage / pagePerBlock);
			this.totalCount = totalCount;
			this.totalPage = (int) Math.ceil((double) totalCount / limitPerPage);
			this.totalBlock = (int) Math.ceil((double) totalPage / pagePerBlock);
			this.offset = (currPage - 1) * limitPerPage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
