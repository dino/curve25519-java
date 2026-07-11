package org.whispersystems.curve25519.java;

public class sc_neg {

  private static byte[] lminus1 = bytes(0xec, 0xd3, 0xf5, 0x5c, 0x1a, 0x63, 0x12, 0x58,
                                    0xd6, 0x9c, 0xf7, 0xa2, 0xde, 0xf9, 0xde, 0x14,
                                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10);

  private static byte[] bytes(int... values) {
    byte[] out = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
        out[i] = (byte) values[i];
    }
    return out;
  }

  public static void sc_neg(byte[] b, byte[] a) {
    byte[] zero = new byte[32];
    sc_muladd.sc_muladd(b, lminus1, a, zero);
  }

}
