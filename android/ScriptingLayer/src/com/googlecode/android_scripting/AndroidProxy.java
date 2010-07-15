/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.android_scripting;

import android.app.Service;
import android.content.Intent;


import com.googlecode.android_scripting.facade.FacadeConfiguration;
import com.googlecode.android_scripting.facade.FacadeManager;
import com.googlecode.android_scripting.jsonrpc.JsonRpcServer;

import java.net.InetSocketAddress;
import java.util.UUID;

public class AndroidProxy {

  private InetSocketAddress mAddress;
  private final JsonRpcServer mJsonRpcServer;
  private final UUID mSecret;

  public AndroidProxy(Service service, Intent intent, boolean requiresHandshake) {
    if (requiresHandshake) {
      mSecret = UUID.randomUUID();
    } else {
      mSecret = null;
    }

    FacadeManager facadeManager =
        new FacadeManager(service, intent, FacadeConfiguration.getFacadeClasses());

    mJsonRpcServer = new JsonRpcServer(facadeManager, getSecret());

  }

  public InetSocketAddress getAddress() {
    return mAddress;
  }

  public InetSocketAddress startLocal() {
    mAddress = mJsonRpcServer.startLocal();
    return mAddress;
  }

  public InetSocketAddress startPublic() {
    mAddress = mJsonRpcServer.startPublic();
    return mAddress;
  }

  public void shutdown() {
    mJsonRpcServer.shutdown();
  }

  String getSecret() {
    if (mSecret == null) {
      return null;
    }
    return mSecret.toString();
  }
}
