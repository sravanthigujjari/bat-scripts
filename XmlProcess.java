package com.justlikethat.custom.xmlprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlProcess {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		args = new String[1];
		args[0] = "micro-medix-sample2.xml";
		if (args.length != 1)
			throw new RuntimeException("The name of the XML file is required!");

		String content = prepareSQL(args[0]);

		File inputFile = new File(args[0]);
		String fullFileName = inputFile.getAbsolutePath();
		String fileName = inputFile.getName();
		fileName = fileName.substring(0, fileName.indexOf("."));
		String parentFolder = fullFileName.substring(0, fullFileName.lastIndexOf("\\"));

		File output = new File(parentFolder + "\\" + fileName + "-output.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		writer.write(content);
		writer.close();

	}

	private static String prepareSQL(String fileName) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new File(fileName));

		NodeList nodeList = document.getDocumentElement().getChildNodes();

		Element informationSectionElement = null;
		Element contentElement = null;

		String externalId = "", drug = "", type = "", typeSub = "", value = "";
		int sectionSubOrder = 1;
		int sectionOrder = 1;

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && "information_section".equals(node.getNodeName())) {
				informationSectionElement = (Element) node;

			} else if (node.getNodeType() == Node.ELEMENT_NODE && "content".equals(node.getNodeName())) {
				contentElement = (Element) node;

			}

		}

		StringBuilder buffer = new StringBuilder();

		Element indexData = (Element) informationSectionElement.getElementsByTagName("index_data").item(0);
		externalId = indexData.getElementsByTagName("document_id").item(0).getTextContent();
		drug = indexData.getElementsByTagName("document_title").item(0).getTextContent();

		try {
			Element brandNameListElement = (Element) indexData.getElementsByTagName("brand_name_list").item(0);

			NodeList brandNameListNames = brandNameListElement.getElementsByTagName("name");
			for (int t = 0; t < brandNameListNames.getLength(); t++) {
				type = "brand_name";
				typeSub = "brand_name";
				value = brandNameListNames.item(t).getTextContent();

				writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
				sectionSubOrder++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			sectionSubOrder = 1;
			Element ndcListElement = (Element) indexData.getElementsByTagName("ndc_list").item(0);
			NodeList ndcListCode = ndcListElement.getElementsByTagName("code");
			for (int t = 0; t < ndcListCode.getLength(); t++) {
				type = "NDC";
				typeSub = "NDC";
				value = removeChars(ndcListCode.item(t).getTextContent(), '-');

				writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
				sectionSubOrder++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			sectionSubOrder = 1;
			Element therapeuticClassListElement = (Element) indexData.getElementsByTagName("therapeutic_class_list")
					.item(0);
			NodeList therapeuticClassListClass = therapeuticClassListElement.getElementsByTagName("class");
			for (int t = 0; t < therapeuticClassListClass.getLength(); t++) {
				type = "therapeutic_class";
				typeSub = "therapeutic_class";
				value = therapeuticClassListClass.item(t).getTextContent();

				writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
				sectionSubOrder++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {

			sectionSubOrder = 1;
			value = contentElement.getElementsByTagName("phonetic").item(0).getTextContent();
			type = "pronounciation";
			typeSub = "pronounciation";
			writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			sectionSubOrder = 1;
			value = contentElement.getElementsByTagName("consumer_route").item(0).getTextContent();
			type = "consumer_route";
			typeSub = "consumer_route";

			writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			sectionSubOrder = 1;
			value = contentElement.getElementsByTagName("indications").item(0).getTextContent();
			type = "Indications";
			typeSub = "Indications";

			writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element contraindicationElement = (Element) contentElement.getElementsByTagName("contraindication").item(0);
			Element contraindicationParaElement = (Element) contraindicationElement.getElementsByTagName("para")
					.item(0);
			type = "contraindications";
			typeSub = "contraindications";
			value = contraindicationParaElement.getTextContent();
			sectionSubOrder = 1;

			writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element howToUseElement = (Element) contentElement.getElementsByTagName("how_to_use").item(0);
			try {

				NodeList consumerFormNodes = howToUseElement.getElementsByTagName("consumer_form");

				sectionSubOrder = 1;
				for (int t = 0; t < consumerFormNodes.getLength(); t++) {
					if (consumerFormNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
						type = "how_to_use";
						typeSub = "consumer_form";
						value = consumerFormNodes.item(t).getTextContent();

						writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
						sectionSubOrder++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			sectionOrder++;
			try {
				Element howToUseListElement = (Element) howToUseElement.getElementsByTagName("list").item(0);
				NodeList howToUseListItemNodes = howToUseListElement.getChildNodes();
				sectionSubOrder = 1;
				for (int t = 0; t < howToUseListItemNodes.getLength(); t++) {
					if (howToUseListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
						type = "how_to_use";
						typeSub = "how_to_use";
						value = howToUseListItemNodes.item(t).getTextContent();

						writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
						sectionSubOrder++;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sectionOrder++;
		try {
			Element missedDoseElement = (Element) contentElement.getElementsByTagName("missed_dose").item(0);
			Element missedDoseListElement = (Element) missedDoseElement.getElementsByTagName("list").item(0);
			NodeList missedDoseListItemNodes = missedDoseListElement.getChildNodes();
			sectionSubOrder = 1;
			for (int t = 0; t < missedDoseListItemNodes.getLength(); t++) {
				if (missedDoseListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
					type = "how_to_use";
					typeSub = "missed_dose";
					value = missedDoseListItemNodes.item(t).getTextContent();

					writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
					sectionSubOrder++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element storageDisposalElement = (Element) contentElement.getElementsByTagName("storage_disposal").item(0);
			Element storageDisposalListElement = (Element) storageDisposalElement.getElementsByTagName("list").item(0);
			NodeList storageDisposalListItemNodes = storageDisposalListElement.getChildNodes();
			sectionSubOrder = 1;
			for (int t = 0; t < storageDisposalListItemNodes.getLength(); t++) {
				if (storageDisposalListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
					type = "how_to_use";
					typeSub = "storage_disposal";
					value = storageDisposalListItemNodes.item(t).getTextContent();

					writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
					sectionSubOrder++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element drugFoodAvoidElement = (Element) contentElement.getElementsByTagName("drug_food_avoid").item(0);
			Element drugFoodAvoidListElement = (Element) drugFoodAvoidElement.getElementsByTagName("list").item(0);
			NodeList drugFoodAvoidListItemNodes = drugFoodAvoidListElement.getChildNodes();
			sectionSubOrder = 1;
			for (int t = 0; t < drugFoodAvoidListItemNodes.getLength(); t++) {
				if (drugFoodAvoidListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
					type = "drug_food_avoid";
					typeSub = "drug_food_avoid";
					value = drugFoodAvoidListItemNodes.item(t).getTextContent();

					writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
					sectionSubOrder++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element warningCautionElement = (Element) contentElement.getElementsByTagName("warning_caution").item(0);
			Element warningCautionListElement = (Element) warningCautionElement.getElementsByTagName("list").item(0);
			NodeList warningCautionListItemNodes = warningCautionListElement.getChildNodes();
			sectionSubOrder = 1;
			for (int t = 0; t < warningCautionListItemNodes.getLength(); t++) {
				if (warningCautionListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
					type = "warning_caution";
					typeSub = "warning_caution";
					value = warningCautionListItemNodes.item(t).getTextContent();

					writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
					sectionSubOrder++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element seriousSideEffectsElement = (Element) contentElement.getElementsByTagName("serious_side_effects")
					.item(0);
			Element seriousSideEffectsListElement = (Element) seriousSideEffectsElement.getElementsByTagName("list")
					.item(0);
			NodeList seriousSideEffectsListItemNodes = seriousSideEffectsListElement.getChildNodes();
			sectionSubOrder = 1;
			for (int t = 0; t < seriousSideEffectsListItemNodes.getLength(); t++) {
				if (seriousSideEffectsListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
					type = "side_effects";
					typeSub = "serious_side_effects";
					value = seriousSideEffectsListItemNodes.item(t).getTextContent();

					writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
					sectionSubOrder++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sectionOrder++;
		try {
			Element lessSeriousSideEffectsElement = (Element) contentElement
					.getElementsByTagName("less_serious_side_effects").item(0);
			Element lessSeriousSideEffectsListElement = (Element) lessSeriousSideEffectsElement
					.getElementsByTagName("list").item(0);
			NodeList lessSeriousSideEffectsListItemNodes = lessSeriousSideEffectsListElement.getChildNodes();
			sectionSubOrder = 1;
			for (int t = 0; t < lessSeriousSideEffectsListItemNodes.getLength(); t++) {
				if (lessSeriousSideEffectsListItemNodes.item(t).getNodeType() == Node.ELEMENT_NODE) {
					type = "side_effects";
					typeSub = "less_serious_side_effects";
					value = lessSeriousSideEffectsListItemNodes.item(t).getTextContent();

					writeToBuffer(sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder, buffer);
					sectionSubOrder++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();

	}

	private static String removeChars(String str, char ch) {
		return str.replaceAll(String.valueOf(ch), "");
	}

	private static void writeToBuffer(int sectionOrder, String externalId, String drug, String type, String typeSub, String value,
			int sectionSubOrder, StringBuilder buffer) {
		buffer.append(
				String.format("%d\t%s\t%s\t%s\t%s\t%s\t%d\n", sectionOrder, externalId, drug, type, typeSub, value, sectionSubOrder++));

	}

}