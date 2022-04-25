package com.smdevisiors.photoboost.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smdevisiors.photoboost.activities.MainActivity
import com.smdevisiors.photoboost.R
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        val activity: MainActivity? = activity as MainActivity?
        val fragmentInfo = activity!!.getInfoFragment()!!
        when (fragmentInfo) {
            "feedback" -> {
                view.info_header_1.text = "Обратная связь"
                view.info_main_1.text = "Для связи с нами пишите пожалуйста на почту: app.photoboost@gmail.com"
                view.info_main_2.visibility = View.INVISIBLE
                view.info_header_2.visibility = View.INVISIBLE
                view.fragment_info.setBackgroundResource(R.drawable.ic_background_1)
            }
            "refund" -> {
                view.info_header_1.text = "Возвраты"
                view.info_main_1.text = "Если вы подписались, используя аккаунт в Google Play Store или напрямую через Пробив номера: свяжитесь со службой поддержки клиентов, указав номер заказа для Google Play Store (номер заказа можно найти в электронном письме с подтверждением заказа или войдя в Google Wallet).\n\nТакже прочитайте правила возврата платежей в Google Play (https://support.google.com/googleplay/answer/2479637#apps)"
                view.info_main_2.visibility = View.INVISIBLE
                view.info_header_2.visibility = View.INVISIBLE
                view.fragment_info.setBackgroundResource(R.drawable.ic_background_2)
            }
            "subscription_control" -> {
                view.info_header_1.text = "Отмена подписки"
                view.info_main_1.text = "Откройте приложение Play Маркет на устройстве Android.\n\nУбедитесь, что вы вошли в правильный аккаунт Google.\n\nНажмите на значок «Меню» → «Подписки».\n\nНайдите подписку, которую нужно отменить.\n\nНажмите «Отменить подписку», следуйте дальнейшим инструкциям."
                view.info_main_2.visibility = View.VISIBLE
                view.info_header_2.visibility = View.VISIBLE
                view.info_header_2.text = "Что происходит после отмены подписки"
                view.info_main_2.text = "При отмене подписки вы будете по-прежнему иметь доступ к контенту до окончания оплаченного периода."
                view.fragment_info.setBackgroundResource(R.drawable.ic_background_3)
            }
        }
        return view
    }
}