/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/security/manager/ssl/public/nsIDataSignatureVerifier.idl
 */

package org.mozilla.interfaces;

/**
 * An interface for verifying that a given string of data was signed by the
 * private key matching the given public key.
 */
public interface nsIDataSignatureVerifier extends nsISupports {

  String NS_IDATASIGNATUREVERIFIER_IID =
    "{0a84b3d5-6ba9-432d-89da-4fbd0b0f2aec}";

  /**
   * Verifies that the data matches the data that was used to generate the
   * signature.
   *
   * @param aData      The data to be tested.
   * @param aSignature The signature of the data, base64 encoded.
   * @param aPublicKey The public part of the key used for signing, DER encoded
   *                   then base64 encoded.
   * @returns true if the signature matches the data, false if not.
   */
  boolean verifyData(String aData, String aSignature, String aPublicKey);

}