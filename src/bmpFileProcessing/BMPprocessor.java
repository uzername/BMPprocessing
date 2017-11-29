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
import helper.UInt16;
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
     * obtain BMP header, typed as one of instances from BMPheaders class
     * @return 
     */
    public BMPHeader getBMPHeader() throws IOException, BMPUnsupportedHeaderSizeException{
        //INTERNAL declarations
       class HeaderProcessor {
            private RandomAccessFile fileAccess;
            
            private void checkHeaderRoutines() {
                if (fileAccess == null) {
                   throw new NullPointerException("file to read should be properly initialized");
                }
            }
            public void initHeaderProcessor(RandomAccessFile in_fileAccess) {
                
                fileAccess = in_fileAccess;
                checkHeaderRoutines();
            }
            /**
             * fill bitmapcoreheader specific data from BMP file, passed earlier to here.
             * @return BMPHeader instance
             * @throws IOException 
             */
            public BMPHeader fill_BITMAPCOREHEADER(BMPHeader header2FillUp) throws IOException {
                checkHeaderRoutines();
                BMPHeader tmpHeader = header2FillUp;
                       int[] fileUINT16value;
                       fileUINT16value = new int[2];
                this.fileAccess.seek(0x12);
                fileUINT16value[0] = this.fileAccess.read(); fileUINT16value[1] = this.fileAccess.read();
                UInt16 tmpPixelWidth = new UInt16(0);
                tmpPixelWidth.AssignUInt16FromByteArrayLSBFirst(fileUINT16value);
                tmpHeader.Width = tmpPixelWidth;
                this.fileAccess.seek(0x14);
                fileUINT16value[0] = this.fileAccess.read(); fileUINT16value[1] = this.fileAccess.read();
                UInt16 tmpPixelHeight = new UInt16(0);
                tmpPixelWidth.AssignUInt16FromByteArrayLSBFirst(fileUINT16value);
                tmpHeader.Height = tmpPixelHeight;
                this.fileAccess.seek(0x16);
                fileUINT16value[0] = this.fileAccess.read(); fileUINT16value[1] = this.fileAccess.read();
                UInt16 tmpPlanesCount = new UInt16(0);
                tmpPlanesCount.AssignUInt16FromByteArrayLSBFirst(fileUINT16value);
                tmpHeader.Planes = tmpPlanesCount;
                this.fileAccess.seek(0x18);
                fileUINT16value[0] = this.fileAccess.read(); fileUINT16value[1] = this.fileAccess.read();
                UInt16 tmpBitPixel = new UInt16(0);
                tmpBitPixel.AssignUInt16FromByteArrayLSBFirst(fileUINT16value);
                tmpHeader.BitCount = tmpBitPixel;
                return tmpHeader;
            }
            public BMPBitmapInfoHeader fill_BITMAPINFOHEADER(BMPHeader in_header2FillUp) throws IOException {
                checkHeaderRoutines();
                 BMPBitmapInfoHeader tmpHeader = new BMPBitmapInfoHeader(in_header2FillUp);
                 int[] fileUINT32value; fileUINT32value = new int[4];
                 int[] fileUINT16value; fileUINT16value = new int[2];
                this.fileAccess.seek(0x12);
                //THE ORDER OF BYTES IN BMP file is reversed! correct: (b[3] << 24) | (b[2] << 16) + (b[1] << 8) + b[0]
                //tmpHeader.Width = this.fileAccess.readInt(); //no good!
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read();
                 tmpHeader.Width = (fileUINT32value[3]<<24) | (fileUINT32value[2]<<16) | (fileUINT32value[1]<<8) + fileUINT32value[0];
                this.fileAccess.seek(0x16);
                //tmpHeader.Height = this.fileAccess.readInt();
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read();
                 tmpHeader.Height = (((fileUINT32value[3])<<24) | ((fileUINT32value[2])<<16) | ((fileUINT32value[1])<<8) + fileUINT32value[0]);
                this.fileAccess.seek(0x1A);
                    fileUINT16value[0] = this.fileAccess.read(); fileUINT16value[1] = this.fileAccess.read();
                    UInt16 tmpPlanesCount = new UInt16(0);
                    tmpPlanesCount.AssignUInt16FromByteArrayLSBFirst(fileUINT16value);
                    tmpHeader.Planes = tmpPlanesCount;
                this.fileAccess.seek(0x1C);
                    fileUINT16value[0] = this.fileAccess.read(); fileUINT16value[1] = this.fileAccess.read();
                    UInt16 tmpBitPixel = new UInt16(0);
                    tmpBitPixel.AssignUInt16FromByteArrayLSBFirst(fileUINT16value);
                    tmpHeader.BitCount = tmpBitPixel;
                this.fileAccess.seek(0x1E);
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read();
                 helper.UInt32 tmpCompression = new UInt32(0);
                 tmpCompression.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
                 tmpHeader.Compression = tmpCompression;
                this.fileAccess.seek(0x22);
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read();
                 helper.UInt32 tmpImageSize = new UInt32(0); tmpImageSize.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
                 tmpHeader.SizeImage = tmpImageSize;
                this.fileAccess.seek(0x26);
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read();
                 tmpHeader.PelsPerMeterX = (int)((((long)fileUINT32value[3])<<24) | (((long)fileUINT32value[2])<<16) | (((long)fileUINT32value[1])<<8) + fileUINT32value[0]);
                this.fileAccess.seek(0x2A);
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read();
                 tmpHeader.PelsPerMeterY = (int)((((long)fileUINT32value[3])<<24) | (((long)fileUINT32value[2])<<16) | (((long)fileUINT32value[1])<<8) + fileUINT32value[0]);                         
                this.fileAccess.seek(0x2E);
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read(); 
                helper.UInt32 tmpNumberColors = new UInt32(0); tmpNumberColors.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
                tmpHeader.ClrUsed = tmpNumberColors;
                this.fileAccess.seek(0x2E);
                 fileUINT32value[0] = this.fileAccess.read(); fileUINT32value[1] = this.fileAccess.read();
                 fileUINT32value[2] = this.fileAccess.read(); fileUINT32value[3] = this.fileAccess.read(); 
                helper.UInt32 tmpImportantColors = new UInt32(0); tmpNumberColors.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
                tmpHeader.ClrImportant = tmpImportantColors;
                return tmpHeader;
            }
       }
       int[] fileUINT32value;
       fileUINT32value = new int[4];

       BMPHeader tmpHeader;
        //end of INTERNAL declarations
       //get the size of header
       this.backendImageToRead.seek(0x0E);
       fileUINT32value[0] = this.backendImageToRead.read(); fileUINT32value[1] = this.backendImageToRead.read();
       fileUINT32value[2] = this.backendImageToRead.read(); fileUINT32value[3] = this.backendImageToRead.read();
       //finding DIB header type by its size
       UInt32 tmpHeaderSize = new UInt32(0);
       tmpHeaderSize.AssignUInt32FromByteArrayLSBFirst(fileUINT32value);
       String headerName = getHeaderNameByLength(tmpHeaderSize.intValue());
       if ( headerName != null ) {
           //new style approach. read core header before checking out other fields
           tmpHeader = new BMPHeader();
           tmpHeader.HeaderSize = tmpHeaderSize;
           HeaderProcessor instHeaderProcessor = new HeaderProcessor();
           instHeaderProcessor.initHeaderProcessor(backendImageToRead);
           tmpHeader = instHeaderProcessor.fill_BITMAPCOREHEADER(tmpHeader);
             //System.out.println(headerName);
           switch(headerName) {
               case("BITMAPCOREHEADER"): {
                 //core header data should be read in all cases. Nothing to do here, just skipping
                 
                 break;
               }
               case("OS22XBITMAPHEADER"): {
                 throw new BMPUnsupportedHeaderSizeException("TODO: handle OS22XBITMAPHEADER");
               }
               case("BITMAPINFOHEADER"): {
                   tmpHeader = instHeaderProcessor.fill_BITMAPINFOHEADER(tmpHeader);
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
