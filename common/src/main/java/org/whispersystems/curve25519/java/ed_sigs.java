package org.whispersystems.curve25519.java;

public class ed_sigs {
    public static int ed25519_verify(Sha512 sha512provider, byte[] signature,
                          byte[] ed25519_pubkey,
                          byte[] msg, int msg_len)
    {
      byte[] verifybuf = new byte[msg_len + 64]; /* working buffer */
      byte[] verifybuf2 = new byte[msg_len + 64]; /* working buffer #2 */

      System.arraycopy(signature, 0, verifybuf, 0, 64);
      System.arraycopy(msg, 0, verifybuf, 64, msg_len);

      return open_modified.crypto_sign_open_modified(sha512provider, verifybuf2, verifybuf, 64 + msg_len, ed25519_pubkey);
    }
}
