package org.example.springpdf.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.springpdf.model.NotifyRequest;
import org.example.springpdf.model.NotifyResult;
import org.example.springpdf.service.StrategyExecutor;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    private final StrategyExecutor<NotifyRequest, NotifyResult> strategyExecutor;

    @GetMapping("/invoice")
    public void createPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            Document document = new Document();
            PdfWriter.getInstance(document, bos);
            document.open();
            document.add(new Paragraph("DUYTRAN.COM"));
            document.add(new Paragraph("Invoice generated at: " + LocalDateTime.now()));
            document.close();
        } catch (DocumentException e) {
            throw new IOException("PDF creation failed", e);
        }
    }

    @PostMapping("/{type}")
    public NotifyResult send(@PathVariable String type, @RequestBody NotifyRequest request) {
        return strategyExecutor.execute(type, request);
    }
}
