package eu.kalnarapps.kalnardict

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.kalnarapps.kalnardict.data.database.getStorageRootPath
import eu.kalnarapps.kalnardict.permissions.PermissionHandler.Companion.requestPermissionsIfNeeded
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionsIfNeeded()
        button.setOnClickListener {
            (getStorageRootPath() + "kalnardict/").apply {
                if (File(this).mkdirs()) {
                    Toast.makeText(
                        this@MainActivity,
                        "created dir: $this",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "not created dir: $this",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
