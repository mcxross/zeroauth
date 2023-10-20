/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero.oauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.mcxross.zero.oauth.AuthorizationManagementActivity.Companion.createResponseHandlingIntent

/**
 * Activity that receives the redirect Uri sent by the OpenID endpoint. It forwards the data
 * received as part of this redirect to [AuthorizationManagementActivity], which
 * destroys the browser tab before returning the result to the completion
 * [android.app.PendingIntent]
 * provided to [DefaultAuthorizationService.performAuthorizationRequest].
 *
 *
 * App developers using this library must override the `appAuthRedirectScheme`
 * property in their `build.gradle` to specify the custom scheme that will be used for
 * the OAuth2 redirect. If custom scheme redirect cannot be used with the identity provider
 * you are integrating with, then a custom intent filter should be defined in your
 * application manifest instead. For example, to handle
 * `https://www.example.com/oauth2redirect`:
 *
 *
 * ```xml
 * <intent-filter>
 * <action android:name="android.intent.action.VIEW"></action>
 * <category android:name="android.intent.category.DEFAULT"></category>
 * <category android:name="android.intent.category.BROWSABLE"></category>
 * <data android:scheme="https" android:host="www.example.com" android:path="/oauth2redirect"></data>
</intent-filter> *
 * ```
 */
class RedirectUriReceiverActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceBundle: Bundle?) {
        super.onCreate(savedInstanceBundle)

        // while this does not appear to be achieving much, handling the redirect in this way
        // ensures that we can remove the browser tab from the back stack. See the documentation
        // on AuthorizationManagementActivity for more details.
        startActivity(
            createResponseHandlingIntent(
                this, intent.data
            )
        )
        finish()
    }
}