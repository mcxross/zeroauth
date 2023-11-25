/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
import xyz.mcxross.zero.crypto.SignatureScheme
import xyz.mcxross.zero.crypto.fromString

object Crypto {

  fun newKeyPair(scheme: SignatureScheme): dynamic {
    return when (scheme) {
      SignatureScheme.Ed25519 -> Ed25519Keypair()
      SignatureScheme.SECP256K1 -> Secp256k1Keypair()
      SignatureScheme.SECP256R1 -> Secp256r1Keypair()
    }
  }

  fun newKeyPair(scheme: String): dynamic {
    return when (fromString(scheme)) {
      SignatureScheme.Ed25519 -> Ed25519Keypair()
      SignatureScheme.SECP256K1 -> Secp256k1Keypair()
      SignatureScheme.SECP256R1 -> Secp256r1Keypair()
    }
  }
}
