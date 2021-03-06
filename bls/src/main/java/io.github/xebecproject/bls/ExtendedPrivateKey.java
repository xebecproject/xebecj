/*
 * Copyright 2018 Dash Core Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file was generated by SWIG (http://www.swig.org) and modified.
 * Version 3.0.12
 */

package io.github.xebecproject.bls;

import com.google.common.base.Preconditions;

import java.util.Arrays;

public class ExtendedPrivateKey extends BLSObject {

  protected ExtendedPrivateKey(long cPtr, boolean cMemoryOwn) {
    super(cPtr, cMemoryOwn);
  }

  protected static long getCPtr(ExtendedPrivateKey obj) {
    return (obj == null) ? 0 : obj.cPointer;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    JNI.delete_ExtendedPrivateKey(cPointer);
  }

  public static ExtendedPrivateKey FromSeed(byte[] seed, long seedLen) {
    Preconditions.checkNotNull(seed);
    Preconditions.checkArgument(seedLen <= seed.length);
    return new ExtendedPrivateKey(JNI.ExtendedPrivateKey_FromSeed(seed, seedLen), true);
  }

  public static ExtendedPrivateKey FromBytes(byte[] serialized) {
    Preconditions.checkNotNull(serialized);
    Preconditions.checkArgument(serialized.length == EXTENDED_PRIVATE_KEY_SIZE);
    return new ExtendedPrivateKey(JNI.ExtendedPrivateKey_FromBytes(serialized), true);
  }

  public ExtendedPrivateKey PrivateChild(long i) {
    return new ExtendedPrivateKey(JNI.ExtendedPrivateKey_PrivateChild(cPointer, this, i), true);
  }

  public ExtendedPublicKey PublicChild(long i) {
    return new ExtendedPublicKey(JNI.ExtendedPrivateKey_PublicChild(cPointer, this, i), true);
  }

  public long GetVersion() {
    return JNI.ExtendedPrivateKey_GetVersion(cPointer, this);
  }

  public short GetDepth() {
    return JNI.ExtendedPrivateKey_GetDepth(cPointer, this);
  }

  public long GetParentFingerprint() {
    return JNI.ExtendedPrivateKey_GetParentFingerprint(cPointer, this);
  }

  public long GetChildNumber() {
    return JNI.ExtendedPrivateKey_GetChildNumber(cPointer, this);
  }

  public ChainCode GetChainCode() {
    return new ChainCode(JNI.ExtendedPrivateKey_GetChainCode(cPointer, this), true);
  }

  public PrivateKey GetPrivateKey() {
    return new PrivateKey(JNI.ExtendedPrivateKey_GetPrivateKey(cPointer, this), true);
  }

  public PublicKey GetPublicKey() {
    return new PublicKey(JNI.ExtendedPrivateKey_GetPublicKey(cPointer, this), true);
  }

  public ExtendedPublicKey GetExtendedPublicKey() {
    return new ExtendedPublicKey(JNI.ExtendedPrivateKey_GetExtendedPublicKey(cPointer, this), true);
  }

  public void Serialize(byte[] buffer) {
    Preconditions.checkNotNull(buffer);
    Preconditions.checkArgument(buffer.length >= EXTENDED_PRIVATE_KEY_SIZE);
    JNI.ExtendedPrivateKey_Serialize__SWIG_0(cPointer, this, buffer);
  }

  public byte [] Serialize() {
    byte [] bytes = new byte[(int)EXTENDED_PRIVATE_KEY_SIZE];
    Serialize(bytes);
    return bytes;
  }

  @Override
  public String toString() {
    return "ExtendedPrivateKey(" + Utils.HEX.encode(Serialize()) + ")";
  }

  public final static long EXTENDED_PRIVATE_KEY_SIZE = JNI.ExtendedPrivateKey_EXTENDED_PRIVATE_KEY_SIZE_get();

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof ExtendedPrivateKey))
      return false;
    ExtendedPrivateKey epk = (ExtendedPrivateKey) obj;
    byte[] theseBytes = new byte[(int) EXTENDED_PRIVATE_KEY_SIZE];
    Serialize(theseBytes);
    byte[] bytes = new byte[(int) EXTENDED_PRIVATE_KEY_SIZE];
    epk.Serialize(bytes);
    return Arrays.equals(theseBytes, bytes);
  }
}
