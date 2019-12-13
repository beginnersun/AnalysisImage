package com.example.kotlinmvvm.vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base_module.bean.UserBean

class MainViewModel : ViewModel() {

    private val userLiveData by lazy {
        MutableLiveData<UserBean>().also {
            loadUser()
        }
    }

    private fun loadUser(){
        userLiveData.value = UserBean("name","password","phoneNumber","headImage")
    }

}