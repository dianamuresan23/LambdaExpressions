package data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitoredData {
	private Date startTime;
	private Date endTime;
	private String activity;
	
	public MonitoredData(String startTime,String endTime,String activity)
	{
		this.activity=activity;
		DateFormat format=new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss"); 
	    try {
			this.setStartTime(format.parse(startTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			this.endTime=format.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	}

	@Override
	public String toString() {
		return "MonitoredData [startTime=" + startTime + ", endTime=" + endTime + ", activity=" + activity + "]";
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getActivity()
	{
		return this.activity;
	}
	public void setActivity(String activity)
	{
		this.activity=activity;
	}

}
