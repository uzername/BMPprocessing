package bmpFileProcessing.BMPheaders;

/**
 * Unofficial format, used sometimes by Photoshop. And BMPSuite. Better use BMPV4 header.
 * @author Ivan
 */
public class BMPBitmapV3Header extends BMPBitmapV2Header {
    helper.UInt32 bV3AlphaMask;
}
