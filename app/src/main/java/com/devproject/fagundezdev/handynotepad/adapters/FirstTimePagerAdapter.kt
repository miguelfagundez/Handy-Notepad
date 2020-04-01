package com.devproject.fagundezdev.handynotepad.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.view.onboarding.OnboardingData

class FirstTimePagerAdapter(var context : Context, var mListPager : ArrayList<OnboardingData>) : PagerAdapter() {


    override fun isViewFromObject(view: View, mOobject: Any): Boolean {
        return view==mOobject
    }

    override fun getCount(): Int {
        return mListPager.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, mObject: Any) {
        container.removeView(mObject as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout : View = inflater.inflate(R.layout.layout_onboarding, null)

        // Connecting with views
        val img = layout.findViewById<ImageView>(R.id.ivImage)
        val title = layout.findViewById<TextView>(R.id.tvOnboardingTitle)
        val description = layout.findViewById<TextView>(R.id.tvOnboardingDescription)

        // Adding data into views
        img.setImageResource(mListPager.get(position).image)
        title.setText(mListPager.get(position).title)
        description.setText(mListPager.get(position).description)

        // Adding the view to the container
        container.addView(layout)

        return layout
    }
}