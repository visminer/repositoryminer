package org.repositoryminer.ast;

import java.util.List;

public class EnumDeclaration extends TypeDeclaration {

	private List<EnumConstantDeclaration> enumConsts;
	
	public EnumDeclaration(){
		this.setType(SoftwareUnitType.ENUM);
	}
	
	/**
	 * @return the enumConsts
	 */
	public List<EnumConstantDeclaration> getEnumConsts() {
		return enumConsts;
	}
	/**
	 * @param enumConsts the enumConsts to set
	 */
	public void setEnumConsts(List<EnumConstantDeclaration> enumConsts) {
		this.enumConsts = enumConsts;
	}

}
