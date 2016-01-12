package tests;

import models.CandidateClassManager;
import models.compiler.cstruct.Attribute;
import models.compiler.cstruct.Module;
import models.javacandidatestruct.CandidateClass;
import models.javacandidatestruct.JavaAttribute;
import models.javacandidatestruct.JavaMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicolas Burroni
 * @since 7/26/2014
 */
public class ConcatTest {
	public static void main(String[] args) {
		List<CandidateClass> ccds = new ArrayList<>();
		CandidateClass ccd = new CandidateClass("TestOne");
		ccd.addAttribute(new JavaAttribute("int", "number", false, 0));
		ccd.addAttribute(new JavaAttribute("char", "charArray", true, 10));
		ccd.addMethod(new JavaMethod("int", "getNumber", new ArrayList<Attribute>(), "return number;"));
		ccds.add(ccd);
		CandidateClassManager manager = new CandidateClassManager(ccds);
		String[][] s = manager.getCandidateClassesToString();
		for (String[] strings : s) {
			for (String string : strings) {
				System.out.println(string);
			}
		}
	}
}
