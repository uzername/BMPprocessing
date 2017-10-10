package bmpFileProcessing.BMPheaders;


/**
 * BMP header
 * @author Ivan
 */
public class BMPBitmapInfoHeader extends BMPHeader {
    
        
	public helper.UInt32 Compression;
        /**
         * total size of image, const offset 0x02.
         */
	public helper.UInt32 SizeImage;
	public Integer PelsPerMeterX;
	public Integer PelsPerMeterY;
	public helper.UInt32 ClrUsed;
	public helper.UInt32 ClrImportant;
	
}
