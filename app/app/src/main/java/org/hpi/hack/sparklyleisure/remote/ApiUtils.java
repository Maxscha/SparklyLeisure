package org.hpi.hack.sparklyleisure.remote;

public class ApiUtils {
 
    private ApiUtils() {}
 
    public static final String BASE_URL = RemoteSettings.getBaseUrl();
 
    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}