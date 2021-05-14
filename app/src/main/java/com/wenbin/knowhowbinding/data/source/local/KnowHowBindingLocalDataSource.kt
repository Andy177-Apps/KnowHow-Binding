package com.wenbin.knowhowbinding.data.source.local

import android.content.Context
import android.util.Log
import com.wenbin.knowhowbinding.data.Article
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.data.Result


class KnowHowBindingLocalDataSource(val context: Context) : KnowHowBindingDataSource {

    override suspend fun createTestedData(): Result<List<Article>> {
        var defaultData = mutableListOf<Article>()
        defaultData.run {
            add(Article(authorName = "Levi",city = "Kaohsiung","Hi, 大家，期末安安，一起來\n" +
                    "研究考古題好嗎？","讀書會"))
            add(Article(authorName = "Jeremy Chang",city = "Taipei","嗨\n" +
                    "目前就讀於劍橋大學教育所博士班（人在台灣），想要多多補充自己關於統計以及量化研究的相關能力！假如是健身巨巨也很歡迎！\n"+
                    "我也很樂意提供你對於英文學習上面的協助，不管是一般英語還是雅思檢定學習，以及英文寫作！","交換技能"))
            add(Article(authorName = "Sylvia",city = "Online","目前為一位在職設計師，正準備年底的研究所申請，之前有補過習、上過一些線上課程，不過對於寫作還是有點卡關，主要是論點以及論述句子的問題。\n" +
                    "\n" +
                    "以下為對家教的期許及自身狀況：\n" +
                    "1.對於考試題型以及技巧都已掌握，目前目標為25+\n" +
                    "2.希望是針對托福的寫作，其他形式的文章不考慮。\n" +
                    "3.期望每週能批改一至兩篇作文（整合+獨立），希望能在課前完成批改，我會在批改完後自己先檢討，讓上課品質可以更集中在問題討論。\n" +
                    "\n" +
                    "費用部分：\n" +
                    "價格部分可以再商量，由於我的預算有限，但也非常理解「尊重專業」，因此也希望不要來戰費用，希望是真的有意願的人再聊聊。我也能提供一些技能來做交換，UI/UX、吉他、自彈自唱、心理學經驗分享等。 \n" +
                    "\n" +
                    "以上，感謝讀到這裡\uD83D\uDE0A","找家教"))
            add(Article(authorName = "Gabi",city = "Taipei","高二物理","找老師"))
        }
        Log.d("DefaultData", "Frist Data = $defaultData")
        return Result.Success(defaultData)
    }


}