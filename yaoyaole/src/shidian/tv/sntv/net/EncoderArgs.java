package shidian.tv.sntv.net;

import shidian.tv.sntv.tools.NativeUtils;
import shidian.tv.sntv.tools.SecretUtils;
import android.util.Base64;

public class EncoderArgs {

	public static String DESdecrypt(String paramString)
	  {
	    String str1 = NativeUtils.get3DESKey();
	    String str2 = new String(SecretUtils.decryptMode(Base64.decode(paramString.getBytes(), 2), str1));
	    NativeUtils.releaseNewString(str1);
	    return str2;
	  }

	  public static String DESencoder(String paramString)
	  {
	    String str1 = NativeUtils.get3DESKey();
	    String str2 = Base64.encodeToString(SecretUtils.encryptMode(paramString.getBytes(), str1), 2);
	    NativeUtils.releaseNewString(str1);
	    return str2;
	  }

	  public static String EncoderArg(String paramString)
	  {
	    byte[] arrayOfByte = paramString.getBytes();
	    return Base64.encodeToString(NativeUtils.EncryptShiftLeft(arrayOfByte, arrayOfByte.length), 2);
	  }
}
