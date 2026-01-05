package org.example.springpdf.service;

public interface IPDFService {
    void createPDF(String filename);

    void createImagePDF(String inImageFilename, String outFilename);
}
