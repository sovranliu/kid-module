package com.xyzq.kid.logic.record.common;

import java.util.List;

/**
 * Created by Brann
 * DATE @ 2017/8/13.
 */
public class ConsolePage<E> {
	public int total;
	public int begin;
	public int limit;
	public List<E> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
