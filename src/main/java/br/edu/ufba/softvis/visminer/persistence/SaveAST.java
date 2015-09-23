package br.edu.ufba.softvis.visminer.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.EnumConstantDeclaration;
import br.edu.ufba.softvis.visminer.ast.EnumDeclaration;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.PackageDeclaration;
import br.edu.ufba.softvis.visminer.ast.Project;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * This class is used to save in database the software units(methods, classes and others) found in one AST.
 */
public class SaveAST {

	private Map<String, Integer> uidMap;
	private SoftwareUnitDAO softUnitDao;
	private Project project;
	private SoftwareUnitDB projectUnit;
	private RepositoryDB repositoryDb;
	
	public RepositoryDB getRepositoryDB(){
		return this.repositoryDb;
	}
	
	public SaveAST(RepositoryDB repositoryDb, EntityManager entityManager){
		
		uidMap = new HashMap<String, Integer>();
		
		softUnitDao = new SoftwareUnitDAOImpl();
		softUnitDao.setEntityManager(entityManager);
		
		this.repositoryDb = repositoryDb;
		projectUnit = softUnitDao.findByUid(repositoryDb.getUid());
		
		project = new Project();
		project.setId(projectUnit.getId());
		project.setName(projectUnit.getName());

	}
	
	/**
	 * @param filePath
	 * @param fileId
	 * @param ast
	 * Saves the software units found in the given AST in database.
	 */
	public void save(String filePath, int fileId, AST ast){
		
		SoftwareUnitDB parent = null;
		ast.setProject(project);
		
		if(ast.getDocument().getPackageDeclaration() != null){
				
			PackageDeclaration pkgDecl = ast.getDocument().getPackageDeclaration();
			String uid = generateUid(repositoryDb.getPath(), null, pkgDecl.getName());
				
			SoftwareUnitDB pkgUnit = getSofwareUnitDB(uid, pkgDecl.getName(), SoftwareUnitType.PACKAGE,
						0, repositoryDb, projectUnit);
			pkgDecl.setId(pkgUnit.getId());
			parent = pkgUnit;	
			
		}else{
			parent = projectUnit;
		}
		
		String docUid = generateUid(repositoryDb.getPath(), parent.getName(), filePath);
		SoftwareUnitDB docUnit = getSofwareUnitDB(docUid, ast.getDocument().getName(), SoftwareUnitType.FILE,
				fileId, repositoryDb, parent);
		ast.getDocument().setId(docUnit.getId());
		
		if(ast.getDocument().getTypes() != null){
			for(TypeDeclaration type : ast.getDocument().getTypes()){
				
				String typeUid = generateUid(repositoryDb.getPath(), filePath, type.getName());
				
				SoftwareUnitDB typeUnit = getSofwareUnitDB(typeUid, type.getName(), type.getType(),
						0, repositoryDb, docUnit);
				type.setId(typeUnit.getId());
				
				if(type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
					processClassOrInterface( (ClassOrInterfaceDeclaration) type, fileId, typeUnit);
				}else if(type.getType() == SoftwareUnitType.ENUM){
					processEnum( (EnumDeclaration) type, typeUnit, fileId);
				}
				
			}
		}
			
	}
	
	// Generate unique id for software units
	private String generateUid(String repositoryPath, String parentName, String softwareUnitName){
		
		String uid = repositoryPath + parentName + softwareUnitName;
		return StringUtils.sha1(uid);
		
	}
	
	// Saves fields and methods in database
	private void processClassOrInterface(ClassOrInterfaceDeclaration type, int fileId, SoftwareUnitDB typeUnit){
		
		if(type.getFields() != null){
			for(FieldDeclaration field : type.getFields()){
				String fieldUid = generateUid(repositoryDb.getPath(), typeUnit.getName(), type.getName()+"."+field.getName());
				SoftwareUnitDB fieldUnit = getSofwareUnitDB(fieldUid, field.getName(), SoftwareUnitType.FIELD,
						fileId, repositoryDb, typeUnit);
				field.setId(fieldUnit.getId());
			}
		}

		if(type.getMethods() != null){
			for(MethodDeclaration method : type.getMethods()){
				String methodUid = generateUid(repositoryDb.getPath(), typeUnit.getName(), type.getName()+"."+method.getName());
				SoftwareUnitDB methodUnit = getSofwareUnitDB(methodUid, method.getName(), SoftwareUnitType.METHOD,
						fileId, repositoryDb, typeUnit);
				method.setId(methodUnit.getId());
			}
		}
		
	}
	
	// Saves enum constants in database
	private void processEnum(EnumDeclaration type, SoftwareUnitDB enumUnit, int fileId){
		
		if(type.getEnumConsts() != null){
			for(EnumConstantDeclaration constDecl : type.getEnumConsts()){
				String constUid = generateUid(repositoryDb.getPath(), enumUnit.getName(), constDecl.getName());
				SoftwareUnitDB constUnit = getSofwareUnitDB(constUid, constDecl.getName(), SoftwareUnitType.ENUM_CONST, fileId,
						repositoryDb, enumUnit);
				constDecl.setId(constUnit.getId());
			}
		}
		
	}
	
	// Saves the given software unit in database
	private SoftwareUnitDB getSofwareUnitDB(String uid, String name, SoftwareUnitType type, int fileId,
			RepositoryDB repoDb, SoftwareUnitDB parent){
		
		SoftwareUnitDB softwareUnitDB;
		
		if(uidMap.containsKey(uid)){
			softwareUnitDB = new SoftwareUnitDB(uidMap.get(uid), name, type, uid);
		}else{				
			
			softwareUnitDB = new SoftwareUnitDB(0, name, type, uid);
			if(fileId != 0)
				softwareUnitDB.setFile(new FileDB(fileId));
			
			softwareUnitDB.setRepository(repoDb);
			softwareUnitDB.setSoftwareUnit(parent);
			softUnitDao.save(softwareUnitDB);
			uidMap.put(uid, softwareUnitDB.getId());
			
		}

		return softwareUnitDB;
		
	}
	
}