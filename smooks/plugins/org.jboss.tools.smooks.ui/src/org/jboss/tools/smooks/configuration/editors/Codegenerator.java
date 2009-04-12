package org.jboss.tools.smooks.configuration.editors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;

public class Codegenerator {
	String basePath = "/home/Dart/Works/eclipse_wtp.3.0.4/eclipse/workspace/jbosstools/org.jboss.tools.smooks.ui/src/org/jboss/tools/smooks/configuration/editors/uitls/temp/";
	String tempContents = "";

	public Codegenerator() {
		try {
			FileReader reader = new FileReader(
				new File(
					"/home/DartPeng/Work/eclipse/new-smooks-editor/org.jboss.tools.smooks.ui/src/org/jboss/tools/smooks/configuration/editors/Template.txt"));
			BufferedReader r = new BufferedReader(reader);
			String line = r.readLine();
			while (line != null) {
				tempContents += line;
				line = r.readLine();
				if (line != null) {
					tempContents += "\n";
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Codegenerator g = new Codegenerator();
		try {
			g.generateCodes(SmooksPackage.eINSTANCE);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateCodes(EPackage epackage) throws IllegalArgumentException,
		IllegalAccessException, IOException {
		Field[] fields = epackage.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			Class<?> clazz = f.getType();
			if (clazz == EClass.class) {
				EClass eClass = (EClass) f.get(epackage);
				if (eClass.isAbstract() || eClass.isInterface()) {
					continue;
				}
				File file = new File(basePath + eClass.getName() + "UICreator.java");
				System.out.println("map.put("+eClass.getName()+"Impl.class, new "+eClass.getName()+"UICreator());");
				if (!file.exists()) {
					file.createNewFile();
					FileWriter writer = new FileWriter(file);
					writer.write(generateContents(eClass, epackage));
					writer.close();
				}
			}
		}
	}

	private String generateContents(EClass eClass, EPackage ePackage) {
		if (tempContents != null) {
			String className = eClass.getName() + "UICreator";
			String t = tempContents;
			if (t.indexOf("${className}") != -1) {
				String s = t.substring(0, t.indexOf("${className}"));
				String es = t.substring(t.indexOf("${className}") + "${className}".length(), t
					.length());
				t = s + className + es;
			}
			String epName = ePackage.getClass().getSimpleName();
			if (epName.endsWith("Impl")) {
				epName = epName.substring(0, epName.indexOf("Impl"));
			}

			String allepName = epName + ".eINSTANCE.get" + eClass.getName();
			List<EAttribute> alist = eClass.getEAllAttributes();
			String attributeMethod = "";
			for (Iterator<?> iterator = alist.iterator(); iterator.hasNext();) {
				EAttribute attribute = (EAttribute) iterator.next();
				String atn = attribute.getName();
				String firstC = new String(new char[]{ atn.toCharArray()[0] });
				firstC = firstC.toUpperCase();
				atn = firstC + atn.substring(1,atn.length());
				String n = allepName + "_" + atn + "()";
				String cn = "if(feature == " + n + "){}\n";
				attributeMethod += cn;
			}
			int index2 = t.indexOf("${attributeMethod}");
			if (index2 != -1) {
				String am1 = t.substring(0, index2);
				String am2 = t.substring(index2 + "${attributeMethod}".length() , t.length());
				t = am1 + attributeMethod + am2;
			}
			return t;
		}
		return "";
	}
}
