/*
 * Class used to process BMP image formats
 * http://www.kalytta.com/bitmap.h
 * http://www.drdobbs.com/architecture-and-design/the-bmp-file-format-part-1/184409517#0106_0058
 * https://en.wikipedia.org/wiki/BMP_file_format
 * https://github.com/nayuki/BMP-IO
 * https://msdn.microsoft.com/ru-ru/library/windows/desktop/dd183388(v=vs.85).aspx - bitmap reference from microsoft
 */
package bmpFileProcessing;

import bmpFileProcessing.BMPheaders.BMPBitmapInfoHeader;
import bmpFileProcessing.BMPheaders.BMPHeader;
import helper.UInt32;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Ivan
 */
public class BMPprocessor {
    /* known constant sizes of headers */
    private HashMap<String, Integer> knownSizesOfHeaders;
    
    public RandomAccessFile backendImageToRead;
    public BMPprocessor() {
        knownSizesOfHeaders = new HashMap<>();
        knownSizesOfHeaders.put("BITMAPCOREHEADER", 12);
        knownSizesOfHeaders.put("OS22XBITMAPHEADER", 64);
        knownSizesOfHeaders.put("BITMAPINFOHEADER", 40);
        knownSizesOfHeaders.put("BITMAPV2INFOHEADER", 52);
        knownSizesOfHeaders.put("BITMAPV3INFOHEADER", 56);
        knownSizesOfHeaders.put("BITMAPV4INFOHEADER", 108);
        knownSizesOfHeaders.put("BITMAPV5INFOHEADER", 124);
    }
    public BMPprocessor initializeFromFilePath(String pathToFile) 
                        throws FileNotFoundException, IOException, BMPbadFileHeaderException {
        this.backendImageToRead = new RandomAccessFile(pathToFile, "rw");
        //check correctness of file
        this.backendImageToRead.seek(0);
        int byte1 = 0; int byte2 = 0;
        try {
        byte1 = backendImageToRead.readUnsignedByte();
        byte2 = backendImageToRead.readUnsignedByte();
        } catch (EOFException e) {
           throw new BMPbadFileHeaderException();
        }
        if ((((byte1 == 'B') && (byte2 == 'M'))||
            ((byte1 == 'B') && (byte2 == 'A'))||
            ((byte1 == 'C') && (byte2 == 'I'))||
            ((byte1 == 'C') && (byte2 == 'P'))||
            ((byte1 == 'I') && (byte2 == 'C'))||
            ((byte1 == 'P') && (byte2 == 'T')) ) == false) {
            throw new BMPbadFileHeaderException("Unsupported file header type: "+String.valueOf(byte1)+String.valueOf(byte2));
        }
        return this;
    }
    /**
     * 
     * @return 
     */
    public BMPHeader getBMPHeader() throws IOException, BMPUnsupportedHeaderSizeException{
       int[] fileUINT32value;
       fileUINT32value = new int[4];
       BMPHeader tmpHeader;
       
       this.backendImageToRead.seek(0x0E);
       fileUINT32value[0] = this.backendImageToRead.read(); fileUINT32value[1] = this.backendImageToRead.read();
       fileUINT32value[2] = this.backendImageToRead.read(); fileUINT32value[3] = this.backendImageToRead.read();
       //finding DIB header type by its size
       UInt32 tmpHeaderSize = new UInt32(0);
       tmpHeaderSize.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
       String headerName = getHeaderNameByLength(tmpHeaderSize.intValue());
       if ( headerName != null ) {
           //new style approach
           switch(headerName) {
               case("BITMAPCOREHEADER"): {
                 //core header data should be read in all cases. 
                 tmpHeader = new BMPHeader();
                 break;
               }
               case("OS22XBITMAPHEADER"): {
                 throw new BMPUnsupportedHeaderSizeException("TODO: handle OS22XBITMAPHEADER");
               }
               case("BITMAPINFOHEADER"): {
                  break;
               }
               case("BITMAPV2INFOHEADER"): {
                  //not supported by MS. undocumented 
                  throw new BMPUnsupportedHeaderSizeException("TODO: handle BITMAPV2INFOHEADER");
               }
               case("BITMAPV3INFOHEADER"): {
                  //not supported by MS. undocumented 
                  throw new BMPUnsupportedHeaderSizeException("TODO: handle BITMAPV3INFOHEADER");
                  
               }
               case("BITMAPV4INFOHEADER"): {
                   
                  break;
               }
               case("BITMAPV5INFOHEADER"): {
                   
                  break;
               }
               default: {
                 throw new BMPUnsupportedHeaderSizeException("No algorithm for DIB header (size"+tmpHeaderSize.toString()+")");
                 
               }
           }      
            this.backendImageToRead.seek(0);
            fileUINT32value[0] = this.backendImageToRead.read(); fileUINT32value[1] = this.backendImageToRead.read();
            fileUINT32value[2] = this.backendImageToRead.read(); fileUINT32value[3] = this.backendImageToRead.read();
            /*
            ((BMPBitmapInfoHeader) tmpHeader).SizeImage = new UInt32(0);
            ((BMPBitmapInfoHeader) tmpHeader).SizeImage.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
            */
            tmpHeader = new BMPHeader();
            return tmpHeader;
           
       } else {
         throw new BMPUnsupportedHeaderSizeException("No algorithm for DIB header (size"+tmpHeaderSize.toString()+")");
       }
       
    }
    
    public String getHeaderNameByLength(Integer declaredLengthOfHeader) {
        for (HashMap.Entry<String, Integer> entry : knownSizesOfHeaders.entrySet()) {
            if (java.util.Objects.equals(declaredLengthOfHeader, entry.getValue())) {
            return entry.getKey();
            }
        }
    return null;
    }
    
    private bmpFileProcessing.BMPheaders.BMPHeader fillCoreHeader( bmpFileProcessing.BMPheaders.BMPHeader in_HeaderData ) {
        return in_HeaderData;
    }
    
    private bmpFileProcessing.BMPheaders.BMPBitmapInfoHeader constructInfoHeader() {
       bmpFileProcessing.BMPheaders.BMPBitmapInfoHeader out_HeaderData = new bmpFileProcessing.BMPheaders.BMPBitmapInfoHeader();
       return out_HeaderData;
    }
}
