package com.selfapps.triptogether

import org.apache.commons.validator.routines.EmailValidator

object Utils {

    fun checkEmail(email: String) = EmailValidator.getInstance().isValid(email.trim())

    fun checkPassword(password: String) = password.trim().length in 6..30 //TODO REPLACE WITH real behavior

    fun checkUserName(password: String) = password.trim().length in 6..30 //TODO REPLACE WITH real behavior





}