package com.yikang.heartmark.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PingGuResult implements Serializable{
	public String result;   //LAST_MONITOR_DATA
	public String hint;  //RESULT_TITLE
	public String per;   //RESULT_PRO_VALUE
	public String risk;   //RESULT_PRO
	public String stable;  //RESULT_STABLE
	public String huli;   //RESULT_NURSE
	public String type;
	
	public String from;
	public static final String HISTORY = "history";
	public static final String INFO = "info";
}
