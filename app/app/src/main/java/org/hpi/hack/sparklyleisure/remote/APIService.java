package org.hpi.hack.sparklyleisure.remote;

import org.hpi.hack.sparklyleisure.model.Issue;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    @Multipart
    @POST("/posts")
    Call<Issue> saveIssue(@Part MultipartBody.Part image,
                          @Part("type") RequestBody type,
                         @Part("rating") RequestBody rating,
                         @Part("lon") RequestBody lon,
                         @Part("lat") RequestBody lat);
}