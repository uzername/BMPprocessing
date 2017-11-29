package bmpFileProcessing.BMPheaders;


/**
 * BMP header. Several fields are redefined from BMPHeader (BitmapCoreHeader, because width and height may be negative, this cause requires different processing approach)
 * Actually this is not theoretically correct to inherit that from  BMPHeader, they are different, but let it be...
 * https://msdn.microsoft.com/ru-ru/library/windows/desktop/dd183376(v=vs.85).aspx
 * @author Ivan
 */
public class BMPBitmapInfoHeader extends BMPHeader {
    
	public Integer Width;
	public Integer Height;
    
	public helper.UInt32 Compression;

	public helper.UInt32 SizeImage;
	public Integer PelsPerMeterX;
	public Integer PelsPerMeterY;
	public helper.UInt32 ClrUsed;
	public helper.UInt32 ClrImportant;

    public BMPBitmapInfoHeader() {
    }
	
    public BMPBitmapInfoHeader(BMPHeader in_Header) {
        this.HeaderSize = in_Header.HeaderSize;
        this.Planes = in_Header.Planes;
        this.BitCount = in_Header.BitCount;
    }
    
    @Override
    public String toString() {
        String result;
        result = super.toString()+"\n";
        result += "BITMAPINFOHEADER:{ Compression: "+Compression.toString()
                 +"; SizeImage: "+SizeImage.toString()
                 +"; PelsPerMeterX: "+PelsPerMeterX.toString()
                 +"; PelsPerMeterY: "+PelsPerMeterY.toString()
                 +"; ClrUsed: "+ClrUsed.toString()
                 +"; ClrImportant: "+ClrImportant.toString()
                 +"}";
        return result;
    }
}
