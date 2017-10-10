package bmpFileProcessing.BMPheaders;

/**
 * The BITMAPV5HEADER structure is the bitmap information header file. It is an extended version of the BITMAPINFOHEADER structure.
 * https://msdn.microsoft.com/ru-ru/library/windows/desktop/dd183381(v=vs.85).aspx
 * @author Ivan
 */
public class BMPBitmapV5Header extends BMPBitmapV4Header {
    //constants relating to color profiles. Check other constants in BitmapV4Header and related classes
    helper.UInt32 bV5Intent;
    helper.UInt32 bV5ProfileData;
    helper.UInt32 bV5ProfileSize;
    
}
