package br.edu.ufba.softvis.visminer.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.Document;
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
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitPK;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitXCommitDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * This class is used to manage the ASTs.
 * Saves in database the software units(methods, classes and others) found in one AST.
 * To mark all the software units that appear in a snapshot and save this relationship in database. 
 */
public class ProcessAST {

	// Uids of the software units that appear in current commit
	private Map<String, Integer> snapshotUids;

	// Uids of the software units that already appear
	private Map<String, Integer> allUids;

	// Number of children of the packages, if a package has no children it is deleted
	private Map<Integer, Integer> pkgChildren;

	private SoftwareUnitDAO softUnitDao;
	private SoftwareUnitXCommitDAO softUnitXCommitDAO;
	private Project project;
	private SoftwareUnitDB projectUnit;
	private RepositoryDB repositoryDb;

	public RepositoryDB getRepositoryDB(){
		return this.repositoryDb;
	}

	public ProcessAST(RepositoryDB repositoryDb, EntityManager entityManager){

		snapshotUids = new HashMap<String, Integer>();
		allUids = new HashMap<String, Integer>();
		pkgChildren = new HashMap<Integer, Integer>();

		softUnitDao = new SoftwareUnitDAOImpl();
		softUnitDao.setEntityManager(entityManager);

		softUnitXCommitDAO = new SoftwareUnitXCommitDAOImpl();
		softUnitXCommitDAO.setEntityManager(entityManager);

		this.repositoryDb = repositoryDb;
		projectUnit = softUnitDao.findByUid(repositoryDb.getUid());

		project = new Project();
		project.setId(projectUnit.getId());
		project.setName(projectUnit.getName());

		snapshotUids.put(projectUnit.getUid(), projectUnit.getId());

	}

	/**
	 * @param filePath
	 * @param fileId
	 * @param ast
	 * If isDelete is false:
	 * Saves the software units found in the given AST in database.
	 * Marks the software units found in the AST as owned by the snapshot.
	 * 
	 * If isDelete is true:
	 * Removes the software units found in the AST from the list of software units owned by the snapshot. 
	 */
	public void process(String filePath, int fileId, AST ast, boolean isDelete){

		SoftwareUnitDB parent = null;
		ast.setProject(project);

		Document doc = ast.getDocument();

		if(doc.getPackageDeclaration() != null){

			PackageDeclaration pkgDecl = doc.getPackageDeclaration();
			String uid = generateUid(repositoryDb.getPath(), null, pkgDecl.getName());

			SoftwareUnitDB pkgUnit = getSofwareUnitDB(uid, pkgDecl.getName(), SoftwareUnitType.PACKAGE,
					0, repositoryDb, projectUnit, isDelete);
			pkgDecl.setId(pkgUnit.getId());
			parent = pkgUnit;	

		}else{
			parent = projectUnit;
		}

		String docUid = generateUid(repositoryDb.getPath(), parent.getName(), filePath);
		SoftwareUnitDB docUnit = getSofwareUnitDB(docUid, doc.getName(), SoftwareUnitType.FILE,
				fileId, repositoryDb, parent, isDelete);
		doc.setId(docUnit.getId());

		if(doc.getMethods() != null){

			for(MethodDeclaration method : doc.getMethods()){
				String methodUid = generateUid(repositoryDb.getPath(), doc.getName(), doc.getName()+"/"+method.getName());
				SoftwareUnitDB methodUnit = getSofwareUnitDB(methodUid, method.getName(), SoftwareUnitType.METHOD,
						fileId, repositoryDb, docUnit, isDelete);
				method.setId(methodUnit.getId());
			}

		}

		if(doc.getTypes() != null){
			for(TypeDeclaration type : doc.getTypes()){

				String typeUid = generateUid(repositoryDb.getPath(), filePath, type.getName());

				SoftwareUnitDB typeUnit = getSofwareUnitDB(typeUid, type.getName(), type.getType(),
						0, repositoryDb, docUnit, isDelete);
				type.setId(typeUnit.getId());

				if(type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
					processClassOrInterface( (ClassOrInterfaceDeclaration) type, fileId,
							typeUnit, isDelete);
				}else if(type.getType() == SoftwareUnitType.ENUM){
					processEnum( (EnumDeclaration) type, typeUnit, fileId, isDelete);
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
	private void processClassOrInterface(ClassOrInterfaceDeclaration type, int fileId,
			SoftwareUnitDB typeUnit, boolean isDelete){

		for(FieldDeclaration field : type.getFields()){
			String fieldUid = generateUid(repositoryDb.getPath(), typeUnit.getName(), type.getName()+"."+field.getName());
			SoftwareUnitDB fieldUnit = getSofwareUnitDB(fieldUid, field.getName(), SoftwareUnitType.FIELD,
					fileId, repositoryDb, typeUnit, isDelete);
			field.setId(fieldUnit.getId());
		}

		for(MethodDeclaration method : type.getMethods()){
			String methodUid = generateUid(repositoryDb.getPath(), typeUnit.getName(), type.getName()+"."+method.getName());
			SoftwareUnitDB methodUnit = getSofwareUnitDB(methodUid, method.getName(), SoftwareUnitType.METHOD,
					fileId, repositoryDb, typeUnit, isDelete);
			method.setId(methodUnit.getId());
		}

	}

	// Saves enum constants in database
	private void processEnum(EnumDeclaration type, SoftwareUnitDB enumUnit,
			int fileId, boolean isDelete){

		for(EnumConstantDeclaration constDecl : type.getEnumConsts()){
			String constUid = generateUid(repositoryDb.getPath(), enumUnit.getName(), constDecl.getName());
			SoftwareUnitDB constUnit = getSofwareUnitDB(constUid, constDecl.getName(), SoftwareUnitType.ENUM_CONST, fileId,
					repositoryDb, enumUnit, isDelete);
			constDecl.setId(constUnit.getId());
		}

	}

	/* if isDelete is false:
	 * Saves the given software unit in database
	 * Puts a software unit once time deleted into snapshoutUids again
	 * 
	 * if isDelete is true:
	 * Removes a software unit from snapshotUids and put into removeUids
	 */
	private SoftwareUnitDB getSofwareUnitDB(String uid, String name, SoftwareUnitType type, int fileId,
			RepositoryDB repoDb, SoftwareUnitDB parent, boolean isDelete){

		if(isDelete && type == SoftwareUnitType.PACKAGE){

			return new SoftwareUnitDB(snapshotUids.get(uid), name, type, uid);

		}else if(isDelete){
			
			countPackageChidren(parent, true);
			if(snapshotUids.containsKey(uid))
				return new SoftwareUnitDB(snapshotUids.remove(uid), name, type, uid);
			else 
				return new SoftwareUnitDB(allUids.remove(uid), name, type, uid);
		}
		
		SoftwareUnitDB softwareUnitDB;
		Integer id = allUids.get(uid);

		if(id != null){

			softwareUnitDB = new SoftwareUnitDB(id, name, type, uid);
			if(!snapshotUids.containsKey(uid)){
				snapshotUids.put(uid, id);
				countPackageChidren(parent, false);
			}

		}else{				

			softwareUnitDB = new SoftwareUnitDB(0, name, type, uid);
			countPackageChidren(parent, false);
			
			if(fileId != 0)
				softwareUnitDB.setFile(new FileDB(fileId));

			softwareUnitDB.setRepository(repoDb);
			softwareUnitDB.setSoftwareUnit(parent);
			softUnitDao.save(softwareUnitDB);
			
			allUids.put(uid, softwareUnitDB.getId());
			snapshotUids.put(uid, softwareUnitDB.getId());

		}

		return softwareUnitDB;

	}

	/* Adds or subtracts the number of package children, if a package has no children it is
	removed from software units that appear in snapshot */  
	private void countPackageChidren(SoftwareUnitDB softUnit, boolean isDelete){

		if(softUnit == null)
			return;

		if(softUnit.getType() == SoftwareUnitType.PACKAGE){

			Integer qtd = pkgChildren.get(softUnit.getId());

			if(isDelete){

				qtd--;
				pkgChildren.put(softUnit.getId(), qtd);
				if(qtd == 0)
					snapshotUids.remove(softUnit.getUid());

			}else if(qtd != null)
				pkgChildren.put(softUnit.getId(), pkgChildren.get(softUnit.getId())+1);
			else
				pkgChildren.put(softUnit.getId(), 1);

		}

	}

	/**
	 * @param commitId
	 * Persists in database the relationship between software unit and commit.
	 */
	public void flushSoftwareUnits(int commitId){

		List<SoftwareUnitXCommitDB> list = new ArrayList<SoftwareUnitXCommitDB>();
		for(Integer softUnitId : snapshotUids.values()){
			SoftwareUnitXCommitPK pk = new SoftwareUnitXCommitPK(softUnitId, commitId);
			SoftwareUnitXCommitDB elem = new SoftwareUnitXCommitDB(pk);
			list.add(elem);
		}
		softUnitXCommitDAO.batchSave(list);
		
	}

}