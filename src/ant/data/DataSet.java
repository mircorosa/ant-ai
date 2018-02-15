package ant.data;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class DataSet {

	String name = "";
	String fileName = "";
	String path = "";
	HashMap<String,ArrayList<Object>> model = new HashMap<>();
	ArrayList<ArrayList<Object>> data = new ArrayList<>();

	public DataSet(String name, String fileName, String path) {
		this.name = name;
		this.fileName = fileName;
		this.path = path;
	}

	abstract protected void initializeStructure();

	abstract protected void generateDataSet();

	void addAttribute(String name, Object ... values) {
		model.put(name,new ArrayList<>(Arrays.asList(values)));
	}

	void addEntry(Object ... values) {
		if(values.length>0)
			data.add(new ArrayList<>((Arrays.asList(values))));
	}

	void printToFile() throws FileNotFoundException {
		PrintWriter arff = new PrintWriter(path+fileName+".arff");

		arff.println("@RELATION "+name);
		arff.println();

		//Definition Section
		for(Map.Entry<String,ArrayList<Object>> entry : model.entrySet()) {
			arff.print("@ATTRIBUTE "+entry.getKey()+" ");
			if(entry.getValue().isEmpty())
				arff.println();
			else if(entry.getValue().size()==1)
				arff.println(entry.getValue().get(0));
			else {
				StringBuilder values = new StringBuilder("{");
				for(Object obj : entry.getValue())
					values.append(obj).append(",");
				values.deleteCharAt(values.length()-1).append("}");
				arff.println(values.toString());
			}
		}
		arff.println();

		//Data Section
		if(!data.isEmpty()) {
			arff.println("@DATA");

			for(ArrayList<Object> record : data) {
				StringBuilder values = new StringBuilder();
				for(Object obj : record)
					values.append(obj).append(",");
				values.deleteCharAt(values.length()-1);
				arff.println(values.toString());
			}
		}
		else
			arff.println("% No data available");

		arff.close();
	}

}
