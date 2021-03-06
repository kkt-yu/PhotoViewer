package kktyu.xyz.testphotoviewer

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.TimeUnit
import kktyu.xyz.testphotoviewer.listResponseDataClass.Rsp
import kktyu.xyz.testphotoviewer.photoInfoResponseDataClass.RspInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GetApiData(url: String) {
    private var retrofit: Retrofit

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getClient())
            .build()
    }

    // リストデータ取得処理
    suspend fun getPhotoList(parameter: Map<String, String>): Response<Rsp> =
            withContext(Dispatchers.IO) {
                val service = retrofit.create(SearchApiInterface::class.java)
                return@withContext service.getMaster(parameter).execute()
            }

    // 写真詳細データ取得処理
    fun getPhotoInfo(parameter: Map<String, String>): Response<RspInfo> {
        val service = retrofit.create(InfoApiInterface::class.java)
        return service.getInfo(parameter).execute()
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }
}
