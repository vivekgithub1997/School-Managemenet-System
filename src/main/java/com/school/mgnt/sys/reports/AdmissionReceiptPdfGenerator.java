package com.school.mgnt.sys.reports;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.school.mgnt.sys.student.management.entity.AdmissionReceipt;

public class AdmissionReceiptPdfGenerator {

	public static byte[] generatePdf(AdmissionReceipt receipt) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			// Add QR code in the top-left corner
			String qrCodeData = generateQrCodeData(receipt);
			Image qrCodeImage = generateQrCode(qrCodeData, 100, 100); // QR code size
			qrCodeImage.setAbsolutePosition(36, document.getPageSize().getHeight() - 136); // Top-left positioning
			document.add(qrCodeImage);

			// Define fonts
			Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK);
			Font subtitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.ITALIC, BaseColor.DARK_GRAY);
			Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);
			Font tableHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE);

			// School slogan and motivational line
			Paragraph slogan = new Paragraph("Welcome to " + receipt.getSchoolName(), titleFont);
			slogan.setAlignment(Element.ALIGN_CENTER);
			document.add(slogan);

			Paragraph motto = new Paragraph("“Empowering Young Minds for a Brighter Future”", subtitleFont);
			motto.setAlignment(Element.ALIGN_CENTER);
			motto.setSpacingAfter(20f);
			document.add(motto);

			// Receipt details (top header)
			PdfPTable headerTable = new PdfPTable(2);
			headerTable.setWidthPercentage(100);
			headerTable.setWidths(new float[] { 50f, 50f });
			document.add(new Paragraph(" "));
			PdfPCell leftCell = new PdfPCell(new Phrase("     Receipt ID: " + receipt.getReceiptId(), normalFont));
			leftCell.setBorder(Rectangle.NO_BORDER);
			leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			headerTable.addCell(leftCell);

			String currentDateTime = LocalDateTime.now().toString().substring(0, 19);
			PdfPCell rightCell = new PdfPCell(new Phrase("Receipt Date: " + currentDateTime, normalFont));
			rightCell.setBorder(Rectangle.NO_BORDER);
			rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			headerTable.addCell(rightCell);

			document.add(headerTable);
			document.add(new Paragraph(" "));

			// Receipt title
			Paragraph title = new Paragraph("----- Admission Receipt -----", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingBefore(10f);
			title.setSpacingAfter(10f);
			document.add(title);

			// Table for receipt details
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);
			table.setWidths(new float[] { 2f, 5f });

			// Header row
			PdfPCell headerCell = new PdfPCell(new Phrase("Field", tableHeaderFont));
			headerCell.setBackgroundColor(new BaseColor(0, 128, 128)); // Teal color
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setPadding(10f);
			table.addCell(headerCell);

			headerCell = new PdfPCell(new Phrase("Details", tableHeaderFont));
			headerCell.setBackgroundColor(new BaseColor(0, 128, 128)); // Teal color
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setPadding(10f);
			table.addCell(headerCell);

			// Rows for standard fields
			addTableRow(table, "School Code", receipt.getSchoolCode(), normalFont);
			addTableRow(table, "School Timing", receipt.getSchoolTiming(), normalFont);
			addTableRow(table, "Registration Id", String.valueOf(receipt.getStudent().getRegistrationId()), normalFont);
			addTableRow(table, "Student Name", receipt.getStudentName(), normalFont);
			addTableRow(table, "Student EmailId", receipt.getStudentEmailId(), normalFont);
			addTableRow(table, "Study Class", "Class- " + receipt.getStudyClass(), normalFont);
			addTableRow(table, "Admission Date", receipt.getAdmissionDate(), normalFont);
			addTableRow(table, "Date of Birth", receipt.getDob(), normalFont);
			addTableRow(table, "Gender", receipt.getGender(), normalFont);

			// Rows for portal links
			addTableRow(table, "Parent Portal", "https://localhost:9091/student-portal.com", receipt.getSchoolName(),
					"/student?login=Id?pwd=xyz", normalFont);

			addTableRow(table, "Library Portal", "https://localhost:9092/library-mgmt.com", receipt.getSchoolName(),
					"/lbrmgmt?login=Id?pwd=xyz", normalFont);

			// Rows for fees
			addTableRow(table, "Admission Fee", "Rs. " + receipt.getAdmissionFee(), normalFont);
			// addTableRow(table, "Monthly Fee", "Rs. " + receipt.getMonthlyFee(),
			// normalFont);

			// Add the table to the document
			document.add(table);

			// Footer with additional information
			Paragraph footerMessage = new Paragraph("Thank you for choosing " + receipt.getSchoolName()
					+ "! We are committed to excellence in education.", normalFont);
			footerMessage.setAlignment(Element.ALIGN_CENTER);
			footerMessage.setSpacingBefore(20f);
			document.add(footerMessage);

			Paragraph contactDetails = new Paragraph("Contact Us:\nMobile: " + receipt.getSchoolMobile() + "\nEmail: "
					+ receipt.getSchoolEmail() + "\nAddress: " + receipt.getSchoolAddress(), normalFont);
			contactDetails.setAlignment(Element.ALIGN_CENTER);
			contactDetails.setSpacingBefore(10f);
			document.add(contactDetails);

			// Add the watermark
			PdfContentByte canvas = writer.getDirectContentUnder();
			addWatermark(canvas, receipt.getSchoolName());

		} catch (DocumentException e) {
			throw new RuntimeException("Error while creating the document: " + e.getMessage(), e);
		} finally {
			document.close();
		}

		return outputStream.toByteArray();
	}

	// Standard field-value row addition
	private static void addTableRow(PdfPTable table, String field, String value, Font font) {
		PdfPCell fieldCell = new PdfPCell(new Phrase(field, font));
		fieldCell.setPadding(10f);
		fieldCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(fieldCell);

		PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
		valueCell.setPadding(8f);
		valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(valueCell);
	}

	// Field-value row for links
	private static void addTableRow(PdfPTable table, String field, String baseUrl, String schoolName, String subLink,
			Font font) {
		String fullLink = baseUrl + subLink;
		addTableRow(table, field, fullLink, font);
	}

	private static void addWatermark(PdfContentByte canvas, String watermarkText) {
		Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 50, Font.BOLD, BaseColor.LIGHT_GRAY);
		Phrase watermark = new Phrase(watermarkText, watermarkFont);

		canvas.saveState();
		PdfGState gState = new PdfGState();
		gState.setFillOpacity(0.3f); // Semi-transparent
		canvas.setGState(gState);

		ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 297.5f, 421, 45);
		canvas.restoreState();
	}

	private static String generateQrCodeData(AdmissionReceipt receipt) {
		return "Receipt ID: " + receipt.getReceiptId() + "\nSchool Code: " + receipt.getSchoolCode()
				+ "\nSchool Timing: " + receipt.getSchoolTiming() + "\nRegistration ID: "
				+ receipt.getStudent().getRegistrationId() + "\nStudent Name: " + receipt.getStudentName()
				+ "\nStudy Class: " + receipt.getStudyClass() + "\nAdmission Date: " + receipt.getAdmissionDate()
				+ "\nDate of Birth: " + receipt.getDob() + "\nGender: " + receipt.getGender() + "\nAdmission Fee: Rs."
				+ receipt.getAdmissionFee() + "\nMonthly Fee: Rs." + receipt.getMonthlyFee();
	}

	private static Image generateQrCode(String qrCodeData, int width, int height) throws Exception {
		BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, width, height);
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(qrImage, "PNG", baos);
		return Image.getInstance(baos.toByteArray());
	}
}
