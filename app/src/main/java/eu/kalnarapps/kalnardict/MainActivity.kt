package eu.kalnarapps.kalnardict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import eu.kalnarapps.kalnardict.androidui.navigation.Navigation
import org.koin.android.ext.android.getKoin
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getKoin().declare(
            findNavController(R.id.main_nav_host_fragment),
            Navigation.navControllerQualifier
        )
        setActionBar(findViewById(R.id.toolbar))
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

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        stopKoin()
        super.onStop()
    }
}
