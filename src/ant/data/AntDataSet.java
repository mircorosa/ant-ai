package ant.data;

public class AntDataSet extends DataSet {

	int m;

	public AntDataSet(String name, String fileName, String path, int m) {
		super(name, fileName, path);
		this.m=m;
		initializeStructure();
	}

	@Override
	protected void initializeStructure() {
		//Naming convention: nrow-ncol
		for (int i = 0; i < m * 2 + 1; i++)
			for (int j = 0; j < m * 2 + 1; j++)
				addAttribute(i+"-"+j,"REAL");
		addAttribute("Direction","N","S","E","W");
	}

	@Override
	protected void generateDataSet() {
		//Nothing here, entries added incrementally
	}

	public void removeLastEntry() {
		data.remove(data.size()-1);
	}
}
