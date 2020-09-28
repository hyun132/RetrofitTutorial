package com.example.apitutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.apitutorial.retrofit.RetrofitManager
import com.example.apitutorial.utils.Constants.TAG
import com.example.apitutorial.utils.RESPOMSE_STATE
import com.example.apitutorial.utils.SEARCH_TYPE
import com.example.apitutorial.utils.onMyTextChanged
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentSearchType: SEARCH_TYPE = SEARCH_TYPE.PHOTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MainActivity - onCreate()")

//        라디오 그룹 가져오기
        search_term_radio_group.setOnCheckedChangeListener { _, checkedId ->
            //switch문
            when (checkedId) {
                R.id.photo_search_radio_button -> {
                    Log.d(TAG, "사진 검색 버튼 클릭")
                    search_term_Text_layout.hint = " 사진검색"
                    search_term_Text_layout.startIconDrawable =
                        resources.getDrawable(R.drawable.image, resources.newTheme())
                    this.currentSearchType = SEARCH_TYPE.PHOTO
                }
                R.id.user_search_radio_button -> {
                    Log.d(TAG, "사용자 검색 버튼 클릭")
                    search_term_Text_layout.hint = " 사용자검색"
                    search_term_Text_layout.startIconDrawable =
                        resources.getDrawable(R.drawable.person, resources.newTheme())
                    this.currentSearchType = SEARCH_TYPE.USER
                }
            }
            Log.d(
                TAG,
                "MainActivity - OnCheckChanged() called / currentSearchType : ${currentSearchType}"
            )
        }
//텍스트가 변경이 되었을때
        search_term_edit_text.onMyTextChanged {
            if (it.toString().count() > 0) {
                frame_search_button.visibility = View.VISIBLE
                // 버튼이 키보드에 가리지 않도록 스크롤뷰를 올린다.
                main_Scrollview.scrollTo(0, 200)
                search_term_Text_layout.helperText=" "
            } else {
                frame_search_button.visibility = View.INVISIBLE
                search_term_Text_layout.helperText = "검색어를 입력해주세요"
            }
            if (it.toString().count() == 12){
                Log.d(TAG,"MainActivity - 에러 띄우기")
                Toast.makeText(this,"12글자 까지만 입력할 수 있습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        //검색 버튼 클릭 시
        frame_search_button.setOnClickListener {
            Log.d(TAG,"MainActivity - 검색 버튼이 클릭되었습니다. / currentSearchType $currentSearchType")

            //검색 api 호출
            RetrofitManager.instance.searchPhotos(
                searchTerm = search_term_edit_text.text.toString(),
                completion = { responseState, responseBody ->
                    when (responseState) {
                        RESPOMSE_STATE.OKAY -> {
                            Log.d(TAG, "api 호출 성공 : $responseBody")
                        }
                        RESPOMSE_STATE.FAIL -> {
                            Toast.makeText(this, "api 호출 에러입니다.", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "api 호출 실패 : $responseBody")
                        }
                    }
                })

            this.handleSearchButtonUi()
        }

    }

    private fun handleSearchButtonUi(){
        frame_search_progress.visibility = View.VISIBLE
        frame_search_button.text =""

        Handler().postDelayed({
            frame_search_progress.visibility = View.INVISIBLE
            frame_search_button.text="검색"
        },1500)
    }
}