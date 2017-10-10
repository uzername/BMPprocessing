/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bmpFileProcessing.BMPheaders;

/**
 * structure for BitmapV4Header. other fields are found in BMPBitmapInfoHeader
 * https://msdn.microsoft.com/ru-ru/library/windows/desktop/dd183380(v=vs.85).aspx
 * @author Ivan
 */
public class BMPBitmapV4Header extends BMPBitmapInfoHeader {
    public helper.UInt32 RedMask;
	public helper.UInt32 GreenMask;
	public helper.UInt32 BlueMask;
	public helper.UInt32 AlphaMask;
	public helper.UInt32 CsType;
	//helper.UInt32[9] Endpoints; // see http://msdn2.microsoft.com/en-us/library/ms536569.aspx
	public helper.UInt32 GammaRed;
	public helper.UInt32 GammaGreen;
	public helper.UInt32 GammaBlue;
}
