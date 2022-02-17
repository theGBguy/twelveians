package np.com.netrakc.twelveians.blogs.di

import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.blogger.Blogger
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import np.com.netrakc.twelveians.blogs.domain.repository.ImgurApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideImgurApi(): ImgurApi {
        return Retrofit.Builder()
            .baseUrl(ImgurApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .build()
            .create(ImgurApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBloggerInstance(): Blogger.Builder {
        return Blogger.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(), null
        )
    }

}

// /home/chiranjeevi/AndroidStudioProjects/key_store
// keytool -list -v -keystore /home/chiranjeevi/AndroidStudioProjects/key_store/twelveians.jks -alias twelveians