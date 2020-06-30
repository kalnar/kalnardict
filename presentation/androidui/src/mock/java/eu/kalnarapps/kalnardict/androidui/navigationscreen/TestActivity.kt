package eu.kalnarapps.kalnardict.androidui.navigationscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import eu.kalnarapps.kalnardict.androidui.R
import eu.kalnarapps.kalnardict.androidui.navigation.Navigation
import org.koin.android.ext.android.getKoin

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onStart() {
        super.onStart()
        getKoin().setProperty(
            Navigation.navControllerQualifier.value,
            findNavController(R.id.test_nav_host_fragment) as NavController
        )
    }

    override fun onBackPressed() {
        findNavController(R.id.test_nav_host_fragment).apply {
            val lastNavigationItemId = this.currentDestination?.id
            popBackStack().let {
                if (!it) {
                    if (lastNavigationItemId == R.id.navigation_screen) {
                        this@TestActivity.finish()
                    } else {
                        this.navigate(
                            R.id.navigation_screen,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.navigation_screen, true)
                                .build()
                        )
                    }
                }
            }
        }
    }
}

