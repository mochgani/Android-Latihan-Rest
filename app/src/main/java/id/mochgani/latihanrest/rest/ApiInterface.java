package id.mochgani.latihanrest.rest;

import id.mochgani.latihanrest.model.KontakModel;
import id.mochgani.latihanrest.model.UserModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by mochgani on 03/12/18.
 */

public interface ApiInterface {
    @GET("kontak")
    Call<KontakModel> getKontak();

    @FormUrlEncoded
    @POST("kontak")
    Call<KontakModel> postKontak(@Field("nama") String nama,
                                 @Field("nomor") String nomor);
    @FormUrlEncoded
    @PUT("kontak")
    Call<KontakModel> putKontak(@Field("id") String id,
                                     @Field("nama") String nama,
                                     @Field("nomor") String nomor);
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "kontak", hasBody = true)
    Call<KontakModel> deleteKontak(@Field("id") String id);

    @FormUrlEncoded
    @POST("user")
    Call<UserModel> postLogin(@Field("username") String username,
                               @Field("password") String password);
}
