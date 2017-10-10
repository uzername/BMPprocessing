package bmpFileProcessing.BMPheaders;

/**
 * BMPCOREHEADER
 * Contains metadata info about this BMP file. The most basic header
 * see http://www.kalytta.com/bitmap.h
 * see http://www.drdobbs.com/architecture-and-design/the-bmp-file-format-part-1/184409517#0106_0058
 * https://msdn.microsoft.com/ru-ru/library/windows/desktop/dd183372(v=vs.85).aspx
 * @author Ivan
 */
public class BMPHeader {
        public helper.UInt32 HeaderSize;
	public Integer Width;
	public Integer Height;
	public helper.UInt16 Planes;
	public helper.UInt16 BitCount;
}
