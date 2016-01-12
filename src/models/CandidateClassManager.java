package models;

import models.compiler.InvalidExpressionException;
import models.compiler.NoSupportedInstructionException;
import models.javacandidatestruct.CandidateClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.compiler.cstruct.Compiler;

/**
 * @author Nicolas Burroni
 * @since 7/23/2014
 */
public class CandidateClassManager {

	private List<CandidateClass> candidateClassList;
	private Compiler myCompiler;
	private int lastKnownPosition;



	public List<CandidateClass> getCandidateClassList() {
		return candidateClassList;
	}

	public Compiler getMyCompiler() {
		return myCompiler;
	}

	//Debugging constructor
	public CandidateClassManager(List<CandidateClass> ccds){
		this.candidateClassList = ccds;
		myCompiler = new Compiler();
		lastKnownPosition = 0;
	}

	public CandidateClassManager(){
		this.candidateClassList = new ArrayList<>();
		myCompiler = new Compiler();
		lastKnownPosition = 0;
	}

	public boolean generateClasses(String legacyCodeFilePath) {
		try {
			myCompiler.run(new File(legacyCodeFilePath));
		} catch (IOException | InvalidExpressionException | NoSupportedInstructionException e) {
			return false;
		}
		candidateClassList.addAll(myCompiler.getCandidates().values());
		return true;
	}

	public List<CandidateClass> getCandidateClasses(){
		List<CandidateClass> ccds = new ArrayList<>();
		for(; lastKnownPosition < candidateClassList.size(); lastKnownPosition++){
			ccds.add(candidateClassList.get(lastKnownPosition));
		}
		return ccds;
	}

	public List<File> getAssociatedFiles(){
		return myCompiler.getAlreadyProcessed();
	}

	public String[][] getCandidateClassesToString(){
		String[][] ccds = new String[candidateClassList.size()][3];
		for (; lastKnownPosition < ccds.length; lastKnownPosition++) {
			CandidateClass ccd = candidateClassList.get(lastKnownPosition);
			ccds[lastKnownPosition][0] = ccd.getName();
			concatList(ccds, lastKnownPosition, 1, ccd.getAttributes());
			concatList(ccds, lastKnownPosition, 2, ccd.getMethods());
		}

		return ccds;
	}

	private void concatList(String[][] destination, int i, int j, List toConcat){
		String concatenated = "";
		for (Object obj : toConcat) {
			concatenated = concatenated.concat(obj.toString() + "\n");  //TODO change JavaMethod's toString?
		}
		destination[i][j] = concatenated;
	}

}
