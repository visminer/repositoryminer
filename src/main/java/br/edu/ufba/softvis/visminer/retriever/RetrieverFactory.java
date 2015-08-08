package br.edu.ufba.softvis.visminer.retriever;

public abstract class RetrieverFactory {

	public static <RetrieverType extends Retriever> RetrieverType create(
			Class<?> clazz) {
		try {
			@SuppressWarnings("unchecked")
			RetrieverType retriever = (RetrieverType) clazz.newInstance();
			
			return retriever;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
