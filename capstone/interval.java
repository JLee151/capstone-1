package capstone;

public class interval{
	
	private int start;
	private int end;
	private int count;
	private float totalTimeDiff;
	
	interval()
	{
		start = -1;
		end = -1;
		count = 0;
	}
	interval(int inTime)
	{
		start = inTime;
		end = inTime;
		count = 1;
	}
	void updateTime(int inTime)
	{
		if(start == -1)
		{
			start = inTime;
			end = inTime;
			count++;
		}else
		{
			end = inTime;
			count++;
		}
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
	public int getCount() {
		return count;
	}
	public float getAverage() {
		return totalTimeDiff/count;
	}
	public void updateTotal(int timeDifference) {
		totalTimeDiff += timeDifference;
	}
};
