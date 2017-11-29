package bmpFileProcessing.BMPheaders;

import helper.UInt16;

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
	public helper.UInt16 Width = new UInt16(0);
	public helper.UInt16 Height = new UInt16(0);
	public helper.UInt16 Planes;
	public helper.UInt16 BitCount;
        @Override
        public String toString() {
            return "BITMAPCOREHEADER: {HeaderSize: "+HeaderSize.toString()
                    +"; Width: "+Width.toString()
                    +"; Height: "+Height.toString()
                    +"Planes: "+Planes.toString()
                    +"BitCount: "+BitCount.toString()+"}";
        }
}
