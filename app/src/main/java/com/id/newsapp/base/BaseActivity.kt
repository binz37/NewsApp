package com.id.newsapp.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.id.newsapp.R

open class BaseActivity : AppCompatActivity() {
    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimationSideTransition()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            when (item.itemId) {
                android.R.id.home -> {
                    super.onBackPressed()
                    overridePendingTransition(R.anim.left_in, R.anim.right_out)
                }
            }
            return true
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }

        return false
    }

     fun initActionBar(toolbar: Toolbar, title: String) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = title
            toolbar.isFocusable = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setAnimationSideTransitionOut()
    }

    open fun hideSoftInputKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Ooops")
            .setMessage(message)
        builder.setPositiveButton(android.R.string.yes, null)
        builder.show()
    }

    fun showErrorDialog(exception: Exception) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Ooops")
            .setMessage(exception.message)
        builder.setPositiveButton(android.R.string.yes, null)
        builder.show()
    }

    private fun showMessageDialog(title: String, message: String, listener: DialogInterface.OnClickListener?) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
        builder.setPositiveButton(android.R.string.yes, listener)
        builder.show()
    }

    private fun setAnimationSideTransition() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    private fun setAnimationSideTransitionOut() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }
}