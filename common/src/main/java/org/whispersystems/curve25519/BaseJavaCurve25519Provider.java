/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 * Copyright (c) 2026 Dino Team
 *
 * Licensed according to the LICENSE file in this repository.
 */

package org.whispersystems.curve25519;

import static org.whispersystems.curve25519.java.fe_edy_to_montx.fe_edy_to_montx;
import static org.whispersystems.curve25519.java.fe_frombytes.fe_frombytes;
import static org.whispersystems.curve25519.java.fe_montx_to_edy.fe_montx_to_edy;
import static org.whispersystems.curve25519.java.fe_tobytes.fe_tobytes;

import org.whispersystems.curve25519.java.Sha512;
import org.whispersystems.curve25519.java.curve_sigs;
import org.whispersystems.curve25519.java.scalarmult;
import org.whispersystems.curve25519.java.xed_sigs;

abstract class BaseJavaCurve25519Provider implements Curve25519Provider {

  private final Sha512               sha512provider;
  private       SecureRandomProvider secureRandomProvider;

  protected BaseJavaCurve25519Provider(Sha512 sha512provider,
                                       SecureRandomProvider secureRandomProvider)
  {
    this.sha512provider       = sha512provider;
    this.secureRandomProvider = secureRandomProvider;
  }

  public abstract boolean isNative();

  public void setRandomProvider(SecureRandomProvider secureRandomProvider) {
    this.secureRandomProvider = secureRandomProvider;
  }

  public byte[] calculateAgreement(byte[] ourPrivate, byte[] theirPublic) {
    byte[] agreement = new byte[32];
    scalarmult.crypto_scalarmult(agreement, ourPrivate, theirPublic);

    return agreement;
  }

  public byte[] generatePublicKey(byte[] privateKey) {
    byte[] publicKey = new byte[32];
    curve_sigs.curve25519_keygen(publicKey, privateKey);

    return publicKey;
  }

  public byte[] generatePrivateKey() {
    byte[] random = getRandom(PRIVATE_KEY_LEN);
    return generatePrivateKey(random);
  }

  public byte[] generatePrivateKey(byte[] random) {
    byte[] privateKey = new byte[32];

    System.arraycopy(random, 0, privateKey, 0, 32);

    privateKey[0]  &= 248;
    privateKey[31] &= 127;
    privateKey[31] |= 64;

    return privateKey;
  }

  public byte[] calculateSignature(byte[] random, byte[] privateKey, byte[] message) {
    byte[] result = new byte[64];

    if (curve_sigs.curve25519_sign(sha512provider, result, privateKey, message, message.length, random) != 0) {
      throw new IllegalArgumentException("Message exceeds max length!");
    }

    return result;
  }

  public byte[] calculateXedSignature(byte[] random, byte[] privateKey, byte[] message) {
    byte[] result = new byte[64];

    if (xed_sigs.xed25519_sign(sha512provider, result, privateKey, message, message.length, random) != 0) {
      throw new IllegalArgumentException("Message exceeds max length!");
    }

    return result;
  }

  public boolean verifySignature(byte[] publicKey, byte[] message, byte[] signature) {
    return curve_sigs.curve25519_verify(sha512provider, signature, publicKey, message, message.length) == 0;
  }

  public byte[] calculateVrfSignature(byte[] random, byte[] privateKey, byte[] message) {
    throw new AssertionError("NYI");
  }

  public byte[] verifyVrfSignature(byte[] publicKey, byte[] message, byte[] signature)
      throws VrfSignatureVerificationFailedException
  {
    throw new AssertionError("NYI");
  }

  public byte[] edToMont(byte[] edKeyBytes) {
    byte[] montKeyBytes = new byte[32];
    int[] y = new int[10], u = new int[10];
    fe_frombytes(y, edKeyBytes);
    fe_edy_to_montx(u, y);
    fe_tobytes(montKeyBytes, u);
    return montKeyBytes;
  }

  public byte[] montToEd(byte[] montKeyBytes) {
    byte[] edKeyBytes = new byte[32];
    int[] y = new int[10], u = new int[10];
    fe_frombytes(u, montKeyBytes);
    fe_montx_to_edy(y, u);
    fe_tobytes(edKeyBytes, y);
    return edKeyBytes;
  }

  public byte[] getRandom(int length) {
    byte[] result = new byte[length];
    secureRandomProvider.nextBytes(result);
    return result;
  }
}
