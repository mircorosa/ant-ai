package ant.data;

public class AntDataSet extends DataSet {

	int m;

	public AntDataSet(String name, int m) {
		super(name);
		this.m=m;
		initializeStructure();
	}

	private void initializeStructure() {
		//Naming convention: nrow-ncol
		for (int i = 0; i < m * 2 + 1; i++)
			for (int j = 0; j < m * 2 + 1; j++)
				if(i!=m || j!=m)
					addAttribute(i+"-"+j,"REAL");
		addAttribute("Direction","N","S","E","W");
	}

	public void removeLastEntry() {
		data.remove(data.size()-1);
	}
}
