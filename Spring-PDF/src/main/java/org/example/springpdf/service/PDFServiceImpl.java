package org.example.springpdf.service;

import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
public class PDFServiceImpl implements IPDFService {
    @Override
    public void createPDF(String filename) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            //create a PDF writer instance and pass the output stream
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

            document.open();
            document.addAuthor("Author-Name");
            document.addCreationDate();

            document.add(new Paragraph("Hello World -- Page 1"));
            document.add(new Paragraph("This is my first PDF."));

            document.newPage();

            document.add(new Paragraph("Hello World -- Page 2"));
            document.close();
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public void createImagePDF(String inImageFilename, String outFilename) {

    }
}
