package com.devproject.fagundezdev.handynotepad.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devproject.fagundezdev.handynotepad.R

/********************************************
 * Fragment - NoteDetailsFragment
 * This fragment will manage app's setting
 *     ----- Under construction -----
 * @author: Miguel Fagundez
 * @date: March 14th, 2020
 * @version: 1.0
 * *******************************************/
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
