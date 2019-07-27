package in.wynk.disco.disco_e_wynk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    @GET("music/v3/unisearch/")
    Call <SearchResponse> getSearchResults(@Query("q") String query);


//    @GET("music/v4/content/") //?id=srch_unisysinfo_M09050867&=song&=en
//    Call <ContentPojo> getContentResult(@Query("id") String id, @Query("type") String type, @Query("lang") String lang);


    @GET("streaming/v4/cscgw/{id}.html")
    Call <ContentPlaybackPojo> getPlaybackResult(@Path("id") String id, @Header("x-bsy-utkn") String utkn, @Header("x-bsy-did") String did);
}
