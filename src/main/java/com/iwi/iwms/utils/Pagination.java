package com.iwi.iwms.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Pagination {
	
	@Schema(description = "페이지당 보여줄 데이터 수")
	private int limitPerPage;
	
	@Schema(description = "블럭당 보여줄 페이지 수")
	private int pagePerBlock;
	
	@Schema(description = "전체 데이터 수")
	private long totalCount;
	
	@Schema(description = "현재 페이지")
	private int currentPage;
	
	@Schema(description = "현재 블럭")
	private int currBlock;
	
	@Schema(description = "전체 페이지 수")
	private int totalPage;
	
	@Schema(description = "전체 블럭 수")
	private int totalBlock;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "DB OFFSET")
	private int offset;
	
	@Builder
	public Pagination(int currentPage, int limitPerPage, long totalCount) {
		try {
			this.limitPerPage = limitPerPage;
			this.pagePerBlock = 10;
			this.currentPage = currentPage;
			this.currBlock = (int) Math.ceil((double) currentPage / pagePerBlock);
			this.totalCount = totalCount;
			this.totalPage = (int) Math.ceil((double) totalCount / limitPerPage);
			this.totalBlock = (int) Math.ceil((double) totalPage / pagePerBlock);
			this.offset = (currentPage - 1) * limitPerPage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
