package com.mvvm.myapplication.meeshotest.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mvvm.myapplication.meeshotest.ui.SessionActivity
import java.util.*

object PermissionsManager {


    private fun getRequiredPermissions(context: Context): Array<String?> {
        return try {
            val info = context.packageManager
                .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }

    fun allPermissionsGranted(context: Context): Boolean {
        for (permission in getRequiredPermissions(context)) {
            if (!isPermissionGranted(context, permission)) {
                return false
            }
        }
        return true
    }

    fun getRuntimePermissions(context: Activity) {
        val allNeededPermissions: MutableList<String?> = ArrayList()
        for (permission in getRequiredPermissions(context)) {
            if (!isPermissionGranted(context, permission)) {
                allNeededPermissions.add(permission)
            }
        }
        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context,
                allNeededPermissions.toTypedArray(),
                SessionActivity.PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String?): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission!!)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun showPermissionDialog(context: Activity) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Please grant requested permissions to continue.")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("OK"
        ) { dialog, _ ->
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + context.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            context.startActivity(i)
            //dismiss the dialog
            dialog.dismiss()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }
}