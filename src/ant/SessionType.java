package ant;

public enum SessionType {

	TRAINING,
	DECISION_TREE,
	NEURAL_NETWORK,
	GENETIC_ALGORITHMS;

	@Override
	public String toString() {
		String name = name();
		String[] splitName = name.split("_");
		StringBuilder builder = new StringBuilder();
		for(String word : splitName)
			builder.append(word.charAt(0)).append(word.substring(1).toLowerCase());
		return builder.toString();
	}

}
