package application;

import java.util.ArrayList;

public class day {
	ArrayList<plu> pluCodes = new ArrayList<plu>();
	private String Date;
	
	day(){
		System.out.println("Vuck Fictor");
		setDate("NANI");
	}
	
	day(ArrayList<Integer> PLUcodes) {
		for(int i = 0; i < PLUcodes.size(); i++) {
			plu tempPLU = new plu(PLUcodes.get(i));
			pluCodes.add(tempPLU);
			//System.out.println(pluCodes.get(i).getPlu());
		}
	}
	
	void updatePLU(int plucode, int timeSec) {
		plu tempPLU = new plu(plucode);
		int index = pluCodes.indexOf(tempPLU);
		for(int i = 0; i < pluCodes.size(); i++) {
			if(pluCodes.get(i).getPlu() == plucode) {
				index = i;
				break;
			}
		}
		pluCodes.get(index).updateIntervals(timeSec);
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}
}
