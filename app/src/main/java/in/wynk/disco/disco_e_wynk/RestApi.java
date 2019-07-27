package in.wynk.disco.disco_e_wynk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    @GET("music/v3/unisearch/")
    Call <SearchResponse> getSearchResults(@Query("q") String query);
}
