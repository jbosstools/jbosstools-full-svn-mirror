/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/intl/uconv/idl/nsIUTF8ConverterService.idl
 */

package org.mozilla.interfaces;

public interface nsIUTF8ConverterService extends nsISupports {

  String NS_IUTF8CONVERTERSERVICE_IID =
    "{249f52a3-2599-4b00-ba40-0481364831a2}";

  /**
   * Ensure that |aString| is encoded in UTF-8.  If not, 
   * convert to UTF-8 assuming it's encoded in |aCharset|
   * and return the converted string in UTF-8.
   *
   * @param aString a string to  ensure its UTF8ness
   * @param aCharset the charset to convert from if |aString| is not in UTF-8
   * @param aSkipCheck determines whether or not to skip 'ASCIIness' and 
   *        'UTF8ness' check. Set this to PR_TRUE only if you suspect that 
   *        aString can be mistaken for ASCII / UTF-8 but is actually NOT 
   *        in ASCII / UTF-8 so that aString has to go through the conversion.
   *        skipping ASCIIness/UTF8ness check.
   *        The most common case is the input is in 7bit non-ASCII charsets
   *        like ISO-2022-JP, HZ or UTF-7 (in its original form or
   *        a modified form used in IMAP folder names).
   * @return the converted string in UTF-8.
   * @throws NS_ERROR_UCONV_NOCONV when there is no decoder for aCharset
   *         or error code of nsIUnicodeDecoder in case of conversion failure
   */
  String convertStringToUTF8(String aString, String aCharset, boolean aSkipCheck);

  /**
   * Ensure that |aSpec| (after URL-unescaping it) is encoded in UTF-8.  
   * If not,  convert it to UTF-8, assuming it's encoded in |aCharset|,  
   * and return the result.
   *
   * <p>Make sure that all characters outside US-ASCII in your input spec 
   * are url-escaped if  your spec is not in UTF-8 (before url-escaping) 
   * because the presence of non-ASCII characters is <strong>blindly</strong>
   * regarded as an indication that your input spec is in unescaped UTF-8
   * and it will be returned without further processing. No valid spec
   * going around in Mozilla code would break this assumption. 
   *
   * <p>XXX The above may change in the future depending on the usage pattern.
   *
   * @param aSpec an url-escaped URI spec to  ensure its UTF8ness
   * @param aCharset the charset to convert from if |aSpec| is not in UTF-8
   * @return the converted spec in UTF-8.
   * @throws NS_ERROR_UCONV_NOCONV when there is no decoder for aCharset
   *         or error code of nsIUnicodeDecoder in case of conversion failure
   */
  String convertURISpecToUTF8(String aSpec, String aCharset);

}