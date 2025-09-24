package com.myapp.demo.testview;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class TestView {
	static int cpt = 0;
	static String retourApi="";

	public static List<org.jsoup.nodes.Attribute> attributesInArray(Element elem1, Element elem2) {
		ArrayList<org.jsoup.nodes.Attribute> list = new ArrayList<>(Arrays.asList());
		boolean exist;
		for (org.jsoup.nodes.Attribute attribute1 : elem1.attributes()) {
			exist = false;
			for (org.jsoup.nodes.Attribute attribute2 : elem2.attributes()) {
				if (attribute1.equals(attribute2))
					exist = true;
			}
			if (!exist)
				list.add(attribute1);
		}

		for (org.jsoup.nodes.Attribute attribute2 : elem2.attributes()) {
			exist = false;
			for (org.jsoup.nodes.Attribute attribute1 : elem1.attributes()) {
				if (attribute2.equals(attribute1))
					exist = true;
			}
			if (!exist)
				list.add(attribute2);
		}

		return list;

	}

	// *** taux de ressemblement attributes
	public static List<org.jsoup.nodes.Attribute> AllNodeAttributes(Element elem1) {
		ArrayList<org.jsoup.nodes.Attribute> list = new ArrayList<>(Arrays.asList());

		for (org.jsoup.nodes.Attribute attribute1 : elem1.attributes()) {
			list.add(attribute1);

		}
		for (Element child : elem1.children()) {
			list.addAll(AllNodeAttributes(child));
		}

		return list;

	}

	// *** Total number of children
	public static int NumberChildrend(Element elem1) {
		int numberchild = 1;
		if (elem1.childrenSize() != 0) {
			for (Element child : elem1.children()) {
				numberchild += NumberChildrend(child);
			}
		}
		return numberchild;

	}

	// return squellette balise
	public static String SqueletteHTML(Element elem1) {
		String tag = "";
		for (Element child : elem1.children()) {
			tag += "<" + child.tagName() + ">" + SqueletteHTML(child) + "</" + child.tagName() + ">";
		}
		return tag;
	}

	// Note squellette balise
	public static int NoteSqueletteHTML(Element elem1, Element elem2) {
		String a = SqueletteHTML(elem1);
		String b = SqueletteHTML(elem2);
		if (a.equalsIgnoreCase(b))
			return 10;
		else
			return 0;
	}

	// Note text node
	public static int NoteTextNode(Element elem1, Element elem2) {
		if (elem1.text().equalsIgnoreCase(elem2.text()))
			return 10;
		else
			return 0;
	}

	public static double NoteNumberChild(Element elem1, Element elem2) {
		int a = NumberChildrend(elem1);
		int b = NumberChildrend(elem2);
		if (a < b)
			return ((double) a / (double) b) * 10;
		else
			return ((double) b / (double) a) * 10;

	}

	public static double NoteNiveauChild(int a, int b) {
		if (a > b)
			return 10 - (a - b);
		else
			return 10 - (b - a);

	}

	public static double NoteAttributes(Element elem1, Element elem2) {
		List<Attribute> list1 = new ArrayList<>(Arrays.asList());
		List<Attribute> list2 = new ArrayList<>(Arrays.asList());
		int nbre_ressemblement = 0;
		list1 = AllNodeAttributes(elem1);
		list2 = AllNodeAttributes(elem2);
		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				if (list1.get(i).equals(list2.get(j))) {
					nbre_ressemblement++;
					break;
				}

			}

		}
		for (int i = 0; i < list2.size(); i++) {
			for (int j = 0; j < list1.size(); j++) {
				if (list2.get(i).equals(list1.get(j))) {
					nbre_ressemblement++;
					break;
				}

			}

		}

		return (double) nbre_ressemblement / (list1.size() + list2.size()) * 10;

	}

	public static double NoteDecisif(Element elem1, Element elem2, int i, int j) {
		double note = 0;
		System.out.println("NoteAttributes(elem1, elem2) : " + NoteAttributes(elem1, elem2));
		System.out.println("NoteNiveauChild : " + NoteNiveauChild(i, j));
		System.out.println("NoteNumberChild : " + NoteNumberChild(elem1, elem2));
		System.out.println("NoteSqueletteHTML : " + NoteSqueletteHTML(elem1, elem2));
		System.out.println("NoteTextNode : " + NoteTextNode(elem1, elem2));
		note = NoteAttributes(elem1, elem2) + NoteNiveauChild(i, j) + NoteNumberChild(elem1, elem2)
				+ NoteSqueletteHTML(elem1, elem2) + NoteTextNode(elem1, elem2);
		return note;

	}

	public  String compare(Element E1, Element E2) throws Exception {

		int i = 0;
		int j = 0;
		cpt++;
		double note = 0;
		double note_sup = 0;
		double note_sup_min = 60;
		int index_remove = 0;
		boolean find_diff = false;
		boolean exist;
		String element_ajoute = "";
//        System.out.println("******************");
//        System.out.println(E2);
//        System.out.println("********************-");
		if (E1.childrenSize() < E2.childrenSize())
			for (Element child2 : E2.children()) {
				exist = false;
				for (Element child1 : E1.children()) {
					note = NoteDecisif(child1, child2, i, j);
					System.out.println(note);
					System.out.println("E1 : " + child1);
					System.out.println("E2 : " + child2);
					if (note > note_sup) {
						note_sup = note;
					}

					i++;
				}
				if (note_sup_min > note_sup) {
					note_sup_min = note_sup;
					find_diff = true;
					index_remove = j;
				}
				j++;

			}
		if (find_diff) {
			System.out.println("we found the difference");
			System.out.println("----------------------------") ;
			System.out.println(E2.child(index_remove));
			retourApi = retourApi + E2.child(index_remove);
			System.out.println("----------------------------");
			E2.child(index_remove).remove();
		}
		int k = 0;
		for (Element child2 : E2.children()) {
			try {
				compare(E1.child(k), child2);
				k++;
			} catch (Exception e) {
				// System.out.println("exc :"+child2);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		// create a JSON object
		ObjectNode JSONObject = mapper.createObjectNode();
		JSONObject.put("message", retourApi);
		String JSONString = mapper.writeValueAsString(JSONObject);
		

		return JSONString;
				//"{\"message\" :\""+retourApi+"\" }";
	}

//	public static void main(String[] args) throws Exception {
//		String template_file = "C:\\Users\\ounim\\Documents\\exercice\\htm1.html";
//		FileReader fw = new FileReader(new File(template_file), StandardCharsets.UTF_8);
//		BufferedReader bw = new BufferedReader(fw);
//
//		String html1 = "";
//		String strCurrentLine1 = "";
//		while ((strCurrentLine1 = bw.readLine()) != null) {
//			html1 = html1 + "\r\n" + strCurrentLine1;
//		}
//		bw.close();
//
//		String template_file2 = "C:\\Users\\ounim\\Documents\\exercice\\htm2.html";
//		FileReader fw2 = new FileReader(new File(template_file2), StandardCharsets.UTF_8);
//		BufferedReader bw2 = new BufferedReader(fw2);
//
//		String html2 = "";
//		String strCurrentLine2 = "";
//		while ((strCurrentLine2 = bw2.readLine()) != null) {
//			html2 = html2 + "\r\n" + strCurrentLine2;
//		}
//		bw2.close();
//		/*
//		 * Element E1 = Jsoup.parse(html1); Element E2 =
//		 * Jsoup.parse(html2);//.getElementById("onetrust-banner-sdk");
//		 * 
//		 * 
//		 * compare(E1, E2); System.out.println(E2);
//		 */
//		Element E1 = Jsoup.parse(html1);
//		Element E2 = Jsoup.parse(html2);
//		// Element E2 =
//		// Jsoup.parse(html2).getElementsByClass("ot-general-width").get(0);
////        System.out.println(AllNodeAttributes(E1));
//		//compare(E1, E2);
//		// System.out.println(E2);
//	}
}
