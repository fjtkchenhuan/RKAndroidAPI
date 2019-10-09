// IgetStaticIP.aidl
package com.ys.myapi;

// Declare any non-default types here with import statements

interface IgetMessage {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getStaticIP();
    String getEthMode();
    boolean getEthStatus();
    boolean isAutoSyncTime();
    String getEthDns1();
    String getEthDns2();
    String getGateway();
    String getNetMask();
    String getDhcpIpAddress();
    boolean isSuccessScreenshot(String path);
    boolean isSetDefaultInputMethodSuccess(String defaultInputMethod);
    String getDefaultInputMethod();
}
