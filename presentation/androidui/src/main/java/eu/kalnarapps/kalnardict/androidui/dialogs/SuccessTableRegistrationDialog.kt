package eu.kalnarapps.kalnardict.androidui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import eu.kalnarapps.kalnardict.androidui.R

class SuccessTableRegistrationDialog : DialogFragment() {

    private val args: SuccessTableRegistrationDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(
            requireContext()
        ).customView(R.layout.simple_dialog_with_ok_button).also {
            it.getCustomView().setUpView()
        }
    }

    private fun View.setUpView() {
        val titleView: TextView = findViewById(R.id.title_content)
        val descriptionView: TextView = findViewById(R.id.simple_dialog_description)
        val button: AppCompatButton = findViewById(R.id.simple_dialog_cta)

        titleView.text = getString(R.string.table_registration_dialog_title)
        descriptionView.text = getString(
            R.string.table_registration_dialog_description,
            args.originalTableName,
            args.dictionaryName
        )
        button.setOnClickListener {
            this@SuccessTableRegistrationDialog.dismiss()
        }
    }
}

