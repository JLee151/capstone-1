package capstone;

import java.util.ArrayList;
import java.util.Iterator;

public class day {
	ArrayList<plu> pluCodes = new ArrayList<plu>();
	
	day(){
		
	}
	day(ArrayList<Integer> PLUcodes){
		for(int i = 0; i < PLUcodes.size(); i++){
				plu tempPLU = new plu(PLUcodes.get(i));
				pluCodes.add(tempPLU);
		}
	}
	public void cleanup()
	{
		
		for (Iterator<plu> iterator = pluCodes.iterator(); iterator.hasNext();) {
		    plu tempCode = iterator.next();
		    tempCode.cleanup();
		    if (tempCode.intervals.isEmpty()) {
		        iterator.remove();
		    }
		}
	}
	
	void updatePLU(int plucode, int timeSec){
		plu tempPLU = new plu(plucode);
		int index = pluCodes.indexOf(tempPLU);
		pluCodes.get(index).updateIntervals(timeSec);	
	}
	

}
