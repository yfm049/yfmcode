package com.pro.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("result")
public class Result{
	@XStreamAsAttribute
	public String ret;
	@XStreamAsAttribute
	public String reason;
	@XStreamAsAttribute
	public String op_clock_used;
	@XStreamAsAttribute
	public String op_clock_start;
}
