package it.freddyfire.diet2;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFMergerUtility;

public class MainController {

	private MainView v;
	private Action openAction, printAction;
	
	public MainController(MainView v) {
		this.v = v;
	}
	
	public Action getOpenAction() {
		if (openAction == null) {
			openAction = new AbstractAction("Open") {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent ae) {
					try {
						File tmpDiet = File.createTempFile("diet-", ".pdf");
						tmpDiet.deleteOnExit();
						createPDF().save(tmpDiet.getAbsolutePath());
						Desktop.getDesktop().open(tmpDiet);
					} catch (Exception e) {
						v.printMessage(e.toString());
					}
				}
			};
		}
		return openAction;
	}
	
	public Action getPrintAction() {
		if (printAction == null) {
			printAction = new AbstractAction("Print") {
				private static final long serialVersionUID = 1L;
				
				@Override
				public void actionPerformed(ActionEvent ae) {
					try {
						createPDF().print();
					} catch (Exception e) {
						v.printMessage(e.toString());
					}
				}
			};
		}
		return printAction;
	}
	
	private PDDocument createPDF() throws IOException {
		PDDocument header = new PDDocument();
		PDPage headerPage = new PDPage();
		header.addPage(headerPage);
		
		PDDocument diet = PDDocument.load("pdf/" + v.getDietName() + ".pdf");
		
		PDPageContentStream contentStream = new PDPageContentStream(header, headerPage);
		contentStream.beginText();
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
		contentStream.moveTextPositionByAmount(90, 700);
		contentStream.drawString("Studio Dentistico Alo\u00EFs Dalmasso di Garzegna");
		
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.moveTextPositionByAmount(120, -20);
		contentStream.drawString("MEDICO CHIRURGO SPECIALISTA");
		
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
		contentStream.moveTextPositionByAmount(-90, -20);
		contentStream.drawString("ODONTOSTOMATOLOGIA e PROTESI DENTARIA");
		
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.moveTextPositionByAmount(-30, -30);
		contentStream.drawString("via della Battaglia, 13");
		contentStream.moveTextPositionByAmount(0, -14);
		contentStream.drawString("12100 Madonna dell'Olmo - CUNEO");
		contentStream.moveTextPositionByAmount(0, -14);
		contentStream.drawString("tel. 0171 411881");
		
		contentStream.setFont(PDType1Font.HELVETICA, 14);
		contentStream.moveTextPositionByAmount(-40, -40);
		contentStream.drawString(DateFormat.getDateInstance(DateFormat.LONG, Locale.ITALIAN).format(new Date()));
		
		contentStream.setFont(PDType1Font.HELVETICA, 14);
		contentStream.moveTextPositionByAmount(0, -40);
		contentStream.drawString("Paziente:");
		
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
		contentStream.moveTextPositionByAmount(80, 0);
		contentStream.drawString(v.getName());
		contentStream.moveTextPositionByAmount(0, -18);
		contentStream.drawString(v.getAddress());
		
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.moveTextPositionByAmount(-50, -40);
		String[] fields = v.getNotes().split(" ");
		String currentString = "";
		for (int i = 0; i < fields.length; i++) {
			if ((currentString.length() + fields[i].length()) * 6 > 500) {
				contentStream.drawString(currentString);
				contentStream.moveTextPositionByAmount(0, -14);
				currentString = fields[i];
			} else {
				currentString += fields[i] + " ";
			}
		}
		contentStream.drawString(currentString);
		contentStream.moveTextPositionByAmount(0, -14);
		
		contentStream.endText();
		contentStream.close();
		
		new PDFMergerUtility().appendDocument(header, diet);
		diet.close();
		return header;
	}
}
