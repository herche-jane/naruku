package com.naruku.corn;

/**
 * 七子表达式 模型
 * @author herche
 * @date 2022/10/31
 */
public class TaskScheduleModel {
	
	/**
	 * 所选作业类型:
	 * 1  -> 每天
	 * 2  -> 每月
	 * 3  -> 每周
	 */
	Integer jobType;
	
	/**一周的哪几天*/
	Integer[] dayOfWeeks;
	
	/**一个月的哪几天*/
	Integer[] dayOfMonths;
	
	/**秒  */
	Integer second;
	
	/**分  */
	Integer minute;
	
	/**时  */
	Integer hour;
	
	public Integer getJobType() {
		return jobType;
	}
	
	public TaskScheduleModel setJobType(Integer jobType) {
		this.jobType = jobType;
		return this;
	}
	
	public Integer[] getDayOfWeeks() {
		return dayOfWeeks;
	}
	
	public TaskScheduleModel setDayOfWeeks(Integer[] dayOfWeeks) {
		this.dayOfWeeks = dayOfWeeks;
		return this;
	}
	
	public Integer[] getDayOfMonths() {
		return dayOfMonths;
	}
	
	public TaskScheduleModel setDayOfMonths(Integer[] dayOfMonths) {
		this.dayOfMonths = dayOfMonths;
		return this;
	}
	
	public Integer getSecond() {
		return second;
	}
	
	public TaskScheduleModel setSecond(Integer second) {
		this.second = second;
		return this;
	}
	
	public Integer getMinute() {
		return minute;
	}
	
	public TaskScheduleModel setMinute(Integer minute) {
		this.minute = minute;
		return this;
	}
	
	public Integer getHour() {
		return hour;
	}
	
	public TaskScheduleModel setHour(Integer hour) {
		this.hour = hour;
		return this;
	}
}
