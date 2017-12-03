/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bmp_prj;

import bmpFileProcessing.BMPUnsupportedHeaderSizeException;
import bmpFileProcessing.BMPbadFileHeaderException;
import bmpFileProcessing.BMPprocessor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class BMP_prj {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BMPprocessor testProcessor = new BMPprocessor();
        bmpFileProcessing.BMPheaders.BMPHeader theHeader;
        java.util.ArrayList<String> folderList = new java.util.ArrayList<String>();
        String rootFolder = "D:\\BMPfileFormat_Research\\Samples\\bmpsuite-master\\bmpsuite-master\\";
        folderList.add(rootFolder+"g\\");    folderList.add(rootFolder+"x\\");    folderList.add(rootFolder+"b\\"); folderList.add(rootFolder+"q\\");
        String currentFolder = java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString();
        java.io.File outputFile = new java.io.File(currentFolder+java.io.File.separator+"output.html");
        System.out.println(outputFile.getAbsolutePath());
        java.io.Writer writer = null;
        
        try {
            writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputFile),"utf-8"));
            writer.write("<html> \n");
            writer.write("<head> <title>Description of files</title> "
                    + "<style> .tableitm { border: 1px solid black} </style> </head> \n");
            writer.write("<body> <h1>Description of files in test suite</h1> \n");
            writer.write("<table class=\"tableitm\">");
            for (String folderName : folderList) {
                writer.write("<tr class=\"tableitm\"> <td class=\"tableitm\" colspan=\"2\">"+folderName+"</td> </tr>");
                java.io.File folder = new java.io.File(folderName);
                java.io.File[] listOfFiles = folder.listFiles();
                
                for (java.io.File file : listOfFiles) {
                    if (file.isFile()) {
                        String fileName = file.getAbsolutePath();
                        writer.write("<tr class=\"tableitm\">");
                        writer.write("<td class=\"tableitm\">"+fileName+"</td>");
                        try {
                        testProcessor.initializeFromFilePath(fileName);
                        theHeader = testProcessor.getBMPHeader();
                        String headerInfo = (theHeader.toString());
                        String replaceAllResult = headerInfo.replaceAll("\n", "<br/>");
                        writer.write("<td class=\"tableitm\">"+replaceAllResult+"</td>");
                        } catch (BMPbadFileHeaderException ex) {
                            //ex.printStackTrace();
                            //Logger.getLogger(BMP_prj.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                            writer.write("<td class=\"tableitm\">"+ex.getClass().getName()+"<br/>"+ex.getMessage()+"</td>");
                        } catch (BMPUnsupportedHeaderSizeException ex) {
                            //ex.printStackTrace();
                            //Logger.getLogger(BMP_prj.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                            writer.write("<td class=\"tableitm\">"+ex.getClass().getName()+"<br/>"+ex.getMessage()+"</td>");
                        } finally {
                        writer.write("</tr>");
                        }
                    }
                }
                
            }

            writer.write("</table> </body> \n");
            writer.write("</html>");
            System.out.println("That's all, folks!");
        } catch (IOException ex) {
            Logger.getLogger(BMP_prj.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        
        
    }
    
}
