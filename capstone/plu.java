package capstone;

import java.util.ArrayList;
import java.util.Iterator;

public class plu{

	private int plu;
	private int threshold = 300;	//allowed time between products in seconds
	private int minCount = 8 ;		//minimum number of items per interval
	int size = -1;
	ArrayList<interval> intervals= new ArrayList<interval>();
	plu()
	{
		plu = -1;
	}
	plu(int plu)
	{
		this.plu = plu;
	}
	void updateIntervals(int inTime)
	{
		if(intervals.isEmpty())
		{
			intervals.add(new interval(inTime));
			size++;
		}else{
			int timeDifference = inTime - intervals.get(size).getEnd();
			if (timeDifference > threshold)
			{
				if(intervals.get(size).getCount() < minCount)
				{
					intervals.remove(size);
					size--;
				}
				intervals.add(new interval(inTime));
				size++;
			}else
			{
				interval currentInterval= intervals.get(size);
				currentInterval.updateTime(inTime);
				currentInterval.updateTotal(timeDifference);
			}
		}
	}
	public void cleanup()
	{
		for (Iterator<interval> iterator = intervals.iterator(); iterator.hasNext();) {
		    interval tempInterval = iterator.next();
		    if (tempInterval.getCount() < minCount) {
		        iterator.remove();
		    }
		}
	}

	public int getPlu() {
		return plu;
	}
	public void setPlu(int plu) {
		this.plu = plu;
	}
};
