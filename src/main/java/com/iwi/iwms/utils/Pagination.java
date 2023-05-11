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
	
	@Schema(description = "전체 페이지 수")
	private int totalPageCount;
	
	@Schema(description = "현재 페이지")
	private int page;
	
	@Schema(description = "현재 블럭")
	private int block;

	@Schema(description = "현재 블럭 시작 페이지")
	private int begin;
	
	@Schema(description = "현재 블럭 마지막 페이지")
	private int end;
	
	@Schema(description = "이전 블럭 마지막 페이지")
	private int prev;
	
	@Schema(description = "다음 블럭 시작 페이지")
	private int next;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "offset")
	private int offset;
	
	@Builder
	public Pagination(int page, int limitPerPage, long totalCount) {
		this.limitPerPage = 10;
		if(limitPerPage > 0) {
			this.limitPerPage = limitPerPage;
		}
		this.pagePerBlock = 10;
		this.totalCount = 0;
		if(totalCount > 0) {
			this.totalCount = totalCount;
		}
		this.totalPageCount = (int) Math.ceil((double) this.totalCount / this.limitPerPage);
		
		this.page = 1;
		if(page > 1) {
			this.page = page;
		}
		this.block = (int) Math.ceil((double) this.page / this.pagePerBlock);
		
		this.end = this.block * this.pagePerBlock;
		if (this.end > this.totalPageCount) {
			this.end = this.totalPageCount;
		}
		this.begin = 1;
		if(this.end - (this.pagePerBlock - 1) > 0) {
			this.begin = this.end - (this.pagePerBlock - 1) + (this.block * this.pagePerBlock - this.end);
		}
		
		this.prev = this.begin - 1;
		if (this.prev < 1) {
			this.prev = 1;	
		}
		this.next = this.end + 1;
		if (this.next > this.totalPageCount) {
			this.next = this.totalPageCount;
		}
		
		this.offset = (this.page - 1) * this.limitPerPage;
	}
}
