package com.wenbin.knowhowbinding.home

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.chip.Chip
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.bindImage
import com.wenbin.knowhowbinding.data.Article
import de.hdodenhof.circleimageview.CircleImageView


@EpoxyModelClass(layout = R.layout.item_article)
abstract class MyEntryModel : EpoxyModelWithHolder<MyEntryModel.EntryHolder>() {
    // Every attribute has to be annotated with @EpoxyAttribute
    // Below attributes allow bridge the gap between views and property of data list by using them in fun bind
    // 宣告的這些變數是連結外面的資料，在 Activity 中用的，等等可以在 fun bind 中去把外部的這些資料丟給 item_xml 的 Views
    @EpoxyAttribute
    var article: Article = Article()

    // 把外部傳進來的資料跟 item_xml 的 Views 做連結
    override fun bind(holder: EntryHolder) {
        holder.chip.text = article.type
        holder.text_time_ago.text = article.createdTime.toString()
        holder.textView_category.text = article.tag
        holder.textView_author_name.text = article.author!!.name
        holder.textView_author_identity.text = article.author!!.identity
        holder.textView_city.text = article.city
        holder.textView_find.text = article.find
        holder.textView_give.text = article.give
        holder.textView_description.text = article.content
        holder.textView_createdTime.text = article.createdTime.toString()
    }

    class EntryHolder : EpoxyHolder() {
        // EpoxyHolder is similar to ViewHolder, connecting item_xml and EpoxyModel.
        lateinit var chip : Chip
        lateinit var text_time_ago : TextView
        lateinit var textView_category : TextView
        lateinit var textView_author_name : TextView
        lateinit var textView_author_identity : TextView
        lateinit var textView_city : TextView
        lateinit var textView_find : TextView
        lateinit var textView_give : TextView
        lateinit var textView_description : TextView
        lateinit var textView_createdTime : TextView



        override fun bindView(itemView: View) {
            chip = itemView.findViewById<Chip>(R.id.chip)
            text_time_ago = itemView.findViewById<TextView>(R.id.text_time_ago)
            textView_category = itemView.findViewById<TextView>(R.id.textView_category)
            textView_author_name = itemView.findViewById<TextView>(R.id.textView_author_name)
            textView_author_identity = itemView.findViewById<TextView>(R.id.textView_author_identity)
            textView_city = itemView.findViewById<TextView>(R.id.textView_city)
            textView_find = itemView.findViewById<TextView>(R.id.textView_find)
            textView_give = itemView.findViewById<TextView>(R.id.textView_give)
            textView_description = itemView.findViewById<TextView>(R.id.text_description)
            textView_createdTime = itemView.findViewById<TextView>(R.id.textView_createdTime)
        }
    }
}