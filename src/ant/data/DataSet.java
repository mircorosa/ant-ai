package ant.data;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for .arff files generation.
 * @author Mirco Rosa
 */
public abstract class DataSet {

	protected String name;

	protected ArrayList<Attribute> model = new ArrayList<>();
	protected ArrayList<ArrayList<Object>> data = new ArrayList<>();

	public DataSet(String name) {
		this.name = name;
	}

	public void addAttribute(String name, Object ... values) {
		model.add(new Attribute(name,new ArrayList<>(Arrays.asList(values))));
	}

	public void addEntry(Object ... values) {
		if(values.length>0)
			data.add(new ArrayList<>((Arrays.asList(values))));
	}

	public void printToFile(String path, String fileName) throws FileNotFoundException {
		PrintWriter arff = new PrintWriter(path+fileName+".arff");

		arff.println("@RELATION "+name);
		arff.println();

		//Definition Section
		for(Attribute attribute : model) {
			arff.print("@ATTRIBUTE "+attribute.getName()+" ");
			if(attribute.getValues().isEmpty())
				arff.println();
			else if(attribute.getValues().size()==1)
				arff.println(attribute.getValues().get(0));
			else {
				StringBuilder values = new StringBuilder("{");
				for(Object obj : attribute.getValues())
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

	public String printToString() {
		StringBuilder arff = new StringBuilder();

		arff.append("@RELATION ").append(name).append("\n");

		//Definition Section
		for(Attribute attribute : model) {
			arff.append("@ATTRIBUTE ").append(attribute.getName()).append(" ");
			if(attribute.getValues().isEmpty())
				arff.append("\n");
			else if(attribute.getValues().size()==1)
				arff.append(attribute.getValues().get(0)).append("\n");
			else {
				StringBuilder values = new StringBuilder("{");
				for(Object obj : attribute.getValues())
					values.append(obj).append(",");
				values.deleteCharAt(values.length()-1).append("}");
				arff.append(values.toString()).append("\n");
			}
		}
		arff.append("\n");

		//Data Section
		if(!data.isEmpty()) {
			arff.append("@DATA"+"\n");

			for(ArrayList<Object> record : data) {
				StringBuilder values = new StringBuilder();
				for(Object obj : record)
					values.append(obj).append(",");
				values.deleteCharAt(values.length()-1);
				arff.append(values.toString()).append("\n");
			}
		}
		else
			arff.append("% No data available\n");

		return arff.toString();
	}

	public String getName() {
		return name;
	}

	private class Attribute {
		private String name;
		private ArrayList<Object> values;

		public Attribute(String name, ArrayList<Object> values) {
			this.name = name;
			this.values = values;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ArrayList<Object> getValues() {
			return values;
		}

		public void setValues(ArrayList<Object> values) {
			this.values = values;
		}
	}

}