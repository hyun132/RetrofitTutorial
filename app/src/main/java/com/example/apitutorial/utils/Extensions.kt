package com.example.apitutorial.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


// 문자열이 json형태인지, json 배열 형태인지
fun String?.isJsonObject():Boolean{
    if(this?.startsWith("{") == true && this.endsWith("}")){
        return true
    }else{
        return false
    }
}

// 문자열이 json형태인지, json 배열 형태인지
fun String?.isJsonArray():Boolean{
    if(this?.startsWith("{") == true && this.endsWith("}")){
        return true
    }else{
        return false
    }
}


// editText에 대한 익스텐션
// completion 으로 넣어준 값 비동기로 자동으로 가져올 수 있음.. .(왜?)
fun EditText.onMyTextChanged(completion: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                completion(editable)
            }

        })
}