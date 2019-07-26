package in.wynk.disco.disco_e_wynk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL_SEARCH = "http://stagingdev.wynk.in/";
    private static final String BASE_URL = "http://stage.wynk.in/";
    private Retrofit mRetrofitSearch;
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofitSearch = new Retrofit.Builder()
                .baseUrl(BASE_URL_SEARCH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public RestApi getSearchApi() {
        return mRetrofitSearch.create(RestApi.class);
    }

    public RestApi getContentApi() {
        return mRetrofit.create(RestApi.class);
    }
}