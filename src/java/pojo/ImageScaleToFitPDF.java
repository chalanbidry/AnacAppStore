/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class ImageScaleToFitPDF {

  public static void main(String[] args) throws Exception{
      Image front = Image.getInstance("logo.png");
      Rectangle pageSize = new Rectangle(780, 525);
      Document document = new Document(pageSize);

      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("ImageScaleToFitPDF.pdf"));
      document.open();

      front.scaleToFit(200, 300);
      front.setAbsolutePosition(500, 100);
      document.add(front);

      document.close();
  }
}