package vinova.intern.nhomxnxx.mexplorer.dialogs


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import vinova.intern.nhomxnxx.mexplorer.R
import java.io.File


class RenameDialog : DialogFragment() {
    private var mListener: DialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val view = LayoutInflater.from(activity)
                .inflate(R.layout.rename_dialog, view as ViewGroup?, false)

        // if text is empty, disable the dialog positive button
        val currentNameText = view.findViewById<View>(R.id.current_name) as EditText
        val path = arguments?.getString(PATH)
        val id = arguments?.getString(ID)
        val isDic = arguments?.getBoolean(DIC)
        val token = arguments?.getString(TOKEN)
        val file = File(path)
        currentNameText.setText(file.name)
        val parent = file.parent

        val newNameText = view.findViewById<View>(R.id.new_name) as EditText
        newNameText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = editable != null && editable.isNotEmpty()
            }
        })

        builder.setTitle(R.string.rename)
        builder.setView(view)
        builder.setPositiveButton(R.string.label_save) { _, _ ->
            val newName = newNameText.text.toString()
            val toPath = if (parent == null) newName else parent + File.separator + newName
            if (isLocal)
                mListener?.onRename(file.path, toPath)
            else
                isDic?.let { mListener?.onReNameCloud(toPath, id.toString(), it, token.toString()) }
        }

        val dialog = builder.create()
        view.post { dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false }
        dialog.setCancelable(false)
        return dialog
    }

    interface DialogListener {
        fun onRename(fromPath: String, toPath: String)
        fun onReNameCloud(newName: String,id:String,isDic:Boolean,token:String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mListener = activity as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement DialogListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {

        private val PATH = "path"
        private val ID = "id"
        private val DIC = "dic"
        private val TOKEN ="token"
        private var isLocal = false
        fun newInstance(path: String): RenameDialog {
            isLocal = true
            val fragment = RenameDialog()
            val args = Bundle()
            args.putString(PATH, path)
            fragment.arguments = args
            return fragment
        }

        fun newInstanceCloud(name:String,id:String,isDic:Boolean,token :String):RenameDialog{
            val fragment = RenameDialog()
            val args = Bundle()
            args.putString(PATH, name)
            args.putString(ID,id)
            args.putBoolean(DIC,isDic)
            args.putString(TOKEN,token)
            fragment.arguments = args
            return fragment
        }
    }

}
