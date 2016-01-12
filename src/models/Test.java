package models;

import models.compiler.cstruct.Attribute;
import models.javacandidatestruct.CandidateClass;
import models.javacandidatestruct.JavaAttribute;
import models.javacandidatestruct.JavaMethod;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Nicolas Burroni
 * @since 5/8/2014
 */
public class Test {
	public static void main(String[] args) {

		CandidateClass ccd = new CandidateClass("TestClass");
		ccd.addAttribute(new JavaAttribute("int", "testInteger", false, 0));
		ccd.addMethod(new JavaMethod("int", "getTestInteger", new ArrayList<Attribute>(), "return testInteger;"));

		//Save file
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("ccd2");
			ObjectOutputStream ou = new ObjectOutputStream(fileOutputStream);
			ou.writeObject(ccd);
			ou.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Load file
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream("ccd2");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ObjectInputStream os = null;
		try {
			os = new ObjectInputStream(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			CandidateClass ccd2 = (CandidateClass) os.readObject();
			System.out.println(ccd2);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
