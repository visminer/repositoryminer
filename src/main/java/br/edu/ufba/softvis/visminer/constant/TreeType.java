package br.edu.ufba.softvis.visminer.constant;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Trees types.
 */
public enum TreeType {

	BRANCH(1), TAG(2);

	private int id;
	
	private TreeType(int id){
		this.id = id;
	}
	
	/**
	 * @return Tree type id.
	 */
	public int getId(){
		return this.id;
	}

	/**
	 * @param id
	 * @return Tree type with given id.
	 */
	public static TreeType parse(int id){

		for(TreeType treeType : TreeType.values()){
			if(treeType.getId() == id){
				return treeType;
			}
		}
		
		throw new IllegalArgumentException("Does not exists TreeType with id "+id);
		
	}
	
}
