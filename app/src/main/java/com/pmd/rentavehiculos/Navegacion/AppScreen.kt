sealed class AppScreens(val ruta: String) {
    object SplashScreen : AppScreens("splash_screen")
    object LoginScreen : AppScreens("login_screen")
    object MainScreen : AppScreens("main_screen")
}
