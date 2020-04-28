package eu.kalnarapps.kalnardict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.kalnarapps.kalnardict.permissions.PermissionHandler.Companion.requestPermissionsIfNeeded

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionsIfNeeded()
    }
}
