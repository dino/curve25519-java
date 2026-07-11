package org.whispersystems.curve25519.java;

public class xed_sigs {
    public static int xed25519_sign(Sha512 sha512provider, byte[] signature_out,
                        byte[] curve25519_privkey,
                        byte[] msg, int msg_len,
                        byte[] random)
    {
      ge_p3 ed_pubkey_point = new ge_p3(); /* Ed25519 pubkey point */
      byte[] A = new byte[32]; /* Ed25519 encoded pubkey */
      byte[] sigbuf = new byte[msg_len + 128]; /* working buffer */
      byte[] a = new byte[32];
      byte[] aneg = new byte[32];
      byte sign_bit = 0;

      /* Convert the Curve25519 privkey to an Ed25519 public key */
      ge_scalarmult_base.ge_scalarmult_base(ed_pubkey_point, curve25519_privkey);
      ge_p3_tobytes.ge_p3_tobytes(A, ed_pubkey_point);

      /* Force Edwards sign bit to zero */
      sign_bit = (byte)((A[31] & 0x80) >> 7);
      System.arraycopy(curve25519_privkey, 0, a, 0, 32);
      sc_neg.sc_neg(aneg, a);
      sc_cmov.sc_cmov(a, aneg, sign_bit);
      A[31] &= 0x7F;

      /* Perform an Ed25519 signature with explicit private key */
      sign_modified.crypto_sign_modified(sha512provider, sigbuf, msg, msg_len, a,
                                         A, random);
      System.arraycopy(sigbuf, 0, signature_out, 0, 64);

       return 0;
    }
}
