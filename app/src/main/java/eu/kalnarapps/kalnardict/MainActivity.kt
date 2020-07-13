package eu.kalnarapps.kalnardict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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

}
