package eu.kalnarapps.kalnardict.android.utils

import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment


abstract class FilePickerFragment : Fragment() {

    protected fun triggerFilePicker() {
//        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
//        chooseFile.type = "*/*"
//        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
//        registerForActivityResult(chooseFile, PICKFILE_RESULT_CODE)
        getContent.launch("*/*")
    }

    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Toast.makeText(requireContext(), "get uri: $uri", Toast.LENGTH_SHORT).show()
    }

}