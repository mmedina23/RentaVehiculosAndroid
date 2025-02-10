import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.navigation.AppNavigation
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RentaVehiculosTheme {
                AppNavigation(navController)
            }
        }
    }
}
