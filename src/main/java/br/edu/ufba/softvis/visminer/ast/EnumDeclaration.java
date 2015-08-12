package br.edu.ufba.softvis.visminer.ast;

import java.util.List;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

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
