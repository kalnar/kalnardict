package eu.kalnarapps.kalnardict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        // TODO: extract commented code to another branch
//        requestPermissionsIfNeeded()
//        button.setOnClickListener {
//            (getStorageRootPath() + "kalnardict/").apply {
//                if (File(this).mkdirs()) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "created dir: $this",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "not created dir: $this",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
    }

    override fun onBackPressed() {
        findNavController(R.id.main_nav_host_fragment).apply {
            val lastNavigationItemId = this.currentDestination?.id
            popBackStack().let {
                if (!it) {
                    if (lastNavigationItemId == R.id.dictionaryQueryScreen) {
                        this@MainActivity.finish()
                    } else {
                        this.navigate(
                            R.id.dictionary_query_navigation,
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
