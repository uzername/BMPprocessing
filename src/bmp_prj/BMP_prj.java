/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bmp_prj;

import bmpFileProcessing.BMPUnsupportedHeaderSizeException;
import bmpFileProcessing.BMPbadFileHeaderException;
import bmpFileProcessing.BMPprocessor;
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
        try {
            testProcessor.initializeFromFilePath("D:\\BMPfileFormat_Research\\Samples\\bmpsuite-master\\bmpsuite-master\\g\\pal1.bmp");
            theHeader = testProcessor.getBMPHeader();
            System.out.println(theHeader.toString());
            System.out.println("That's all folks!");
        } catch (IOException ex) {
            Logger.getLogger(BMP_prj.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BMPbadFileHeaderException ex) {
            Logger.getLogger(BMP_prj.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BMPUnsupportedHeaderSizeException ex) {
            Logger.getLogger(BMP_prj.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
