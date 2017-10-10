package bmpFileProcessing.BMPheaders;

/**
 * Unofficial and undocumented header format. Parameters assumed from this table:
 * https://en.wikipedia.org/wiki/BMP_file_format#DIB_header_.28bitmap_information_header.29
 * @author Ivan
 */
public class BMPBitmapV2Header extends BMPBitmapInfoHeader {
  helper.UInt32    bV2RedMask;
  helper.UInt32    bV2GreenMask;
  helper.UInt32    bV2BlueMask;
}
