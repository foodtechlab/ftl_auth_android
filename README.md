# FTLAuth

Библиотека для организации процесса авторизации в Вашем приложении.

## Features

- **Аутентификация по одноразовому коду**. Действие выполняется в два шага: для начала надо инициализировать аутентификацию, передав на вход номер телефона, затем     полученный SMS-код используется для завершения аутенификации
- **Аутентификация по паролю (username + password)**. В качестве username может вытсупать email

## Installation

Для подключения библиотеки в проект необходимо добавить зависимость в app/build.gradle

```sh
implementation "com.foodtechlab.auth:auth:$actual_version"
```

## Handle exceptions

Для обработки исключений в библиотеке имеется интерфейс, ссылка на реализацию которого хранится в `AuthManager`.

```sh
interface ExceptionHandlerListener {

    fun showMessage(
        message: String?,
        title: String? = null,
        posBtnText: String? = null,
        negBtnText: String? = null,
        negBtnAction: () -> Unit = {},
        posBtnAction: () -> Unit = {},
        showSupportButton: Boolean = false,
        cancellable: Boolean = true,
        shouldReauthorize: Boolean = false
    )
}
```

Допустим, на экране ввода телефона мы выполняем запрос на инициализацию аутентификации. Для обработки возможных исключений, нам будет достаточно присвоить переменной `listener` в `AuthManager` ссылку на реализацию интерфейса `ExceptionHandlerListener`

```sh
class PhoneEntryPresenter : BasePresenter<AppView>(), ExceptionHandlerListener {

    @Inject
    lateinit var authManager: AuthManager

    init {
        DIManager.getAppSubcomponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        authManager.listener = this
    }

    override fun onDestroy() {
        DIManager.removeAppSubcomponent()
        authManager.listener = null
        super.onDestroy()
    }

    override fun showMessage(
        message: String?,
        title: String?,
        posBtnText: String?,
        negBtnText: String?,
        negBtnAction: () -> Unit,
        posBtnAction: () -> Unit,
        showSupportButton: Boolean,
        cancellable: Boolean,
        shouldReauthorize: Boolean
    ) {
        viewState.showMessage(
            message,
            title,
            posBtnText,
            negBtnText,
            negBtnAction,
            posBtnAction,
            showSupportButton,
            cancellable,
            shouldReauthorize
        )
    }
    
    fun initAuth() {
        presenterScope.launch {
            // если возникнет исключение, то мы отобразим сообщение, полученное в showMessage
            val timer = authManager.initSms("89029999999")
            viewState.showTimer(timer)
        }
    }
}
```

В случае, если в приложении необходимо выполнить запрос с проверкой авторизации пользователя, то можно использовать метод `tryWithAuthChecking`, который принимает на вход два параметра: синглтон `AuthManager`, который вы создали в проекте, и `block` - блок кода, перед выполнением которого необходимо осуществить проверку авторизации. Если при проверке выяснится, что accessToken истек, то в `tryWithAuthChecking` будет произведена автоматическая попытка рефреша.

## How to use

Для использования возможностей библиотеки необходимо создать синглтон `AuthManager`. Каким образом Вы это сделаете, зависит только от правил, установленных на Вашем проекте.

В наших проектах используется Dagger для внедрения зависимостей, и поэтому создание синглтона `AuthManager` выглядит следующим образом:

```sh
@Module
object ApplicationModule {

    @Provides
    @Singleton
    fun provideAuthManager(
        prefs: SharedPreferences,
        okHttpClient: OkHttpClient,
        context: Context
    ): AuthManager {
        return AuthManager(
            baseUrl = BuildConfig.SERVER,
            apiVersion = ApiService.NOT_SECURE_API_V1,
            sharedPrefs = prefs,
            okHttpClient = okHttpClient,
            applicationContext = context
        )
    }
}
```

Далее в зависимости от ваших предпочтений можно пойти ***двумя путями***:

**Если готовы напрямую использовать не покрытый автотестами AuthManager**, то внедряем зависимость `AuthManager` в нужные нам презентеры

```sh
class MainPresenter : MvpPresenter<MainView>(), ExceptionHandlerListener {

    @Inject
    lateinit var authManager: AuthManager

    init {
        DIManager.addMainSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.removeMainSubcomponent()
        super.onDestroy()
    }

    override fun onFirstViewAttach() {
        authManager.listener = this
        authSms()
    }

    private fun authSms() {
        presenterScope.launch {
            try {
                authManager.initSms("89029999999")
                authManager.loginSms("12345", "89029999999")
            } catch (e: Exception) {
                logError(TAG, e.formatError().second)
            }
        }
    }
}
```

**Если же предпочитаете использовать покрытые автотестами usecase'ы**, то сначала подключим наш модуль `AuthModule`, в котором провайдятся необходимые сущности

```sh
@Singleton
@Component(
    modules = [
        AuthModule::class,
        // other modules
    ]
)
interface ApplicationComponent {
    // some code here
}
```

а затем уже внедряем наши usecase'ы в презентеры

```sh
class SplashPresenter : BasePresenter<SplashView>() {

    @Inject
    lateinit var checkAuthUseCase: CheckAuthUseCase
    
    @Inject
    lateinit var saveAccessTokenUseCase: SaveAccessTokenUseCase

    @Inject
    lateinit var saveRefreshTokenUseCase: SaveRefreshTokenUseCase
    
     init {
        DIManager.getSplashSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.removeSplashSubcomponent()
        super.onDestroy()
    }
    
    private fun rewriteTokens() {
        if (!checkAuthUseCase.execute()) {
            val accessToken = // get old access token from shared prefs
            val refreshToken = // get old refresh token from shared prefs

            saveAccessTokenUseCase.execute(accessToken)
            saveRefreshTokenUseCase.execute(refreshToken)
            
            // remove old access token from shared prefs
            // remove old refresh token from shared prefs
        }
    }
}
```

# License

```
   Copyright 2021 FoodTech Lab

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
