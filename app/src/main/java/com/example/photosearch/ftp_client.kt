package com.example.photosearch

import android.content.Context
import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception

class ftp_client {
    private val TAG = "ftp_client"
    lateinit var ftp: FTPClient

    fun connect(ip: String, user: String, password: String) : Boolean{
        var status = false
        try{
            ftp = FTPClient()
            ftp.connect(ip, 21)
            if(FTPReply.isPositiveCompletion(ftp.replyCode)){
                status = ftp.login(user, password)
                ftp.setFileType(FTP.BINARY_FILE_TYPE)
                ftp.enterLocalPassiveMode()
                Log.i(TAG, "FTP: Подключен")
                return status
            }
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG, "connect method: ", e)
        }
        return status
    }

    fun disconnect() : Boolean{
        try{
            ftp.logout()
            ftp.disconnect()
            Log.i(TAG, "FTP: Отключение")
            return true
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG, "disconnect method: ", e)
        }
        return false
    }

    fun upload(filePath: String, dirPath: String, context: Context) : Boolean{
        var status = false
        try{
            val inputStream: InputStream = FileInputStream(dirPath)
            status = ftp.storeFile(filePath, inputStream)
            inputStream.close()
            Log.i(TAG, "FTP: Файл '$filePath' был загружен на сервер")
            return status
        } catch (e: Exception){
            e.printStackTrace()
            Log.e(TAG, "upload method: ", e)

        }
        return status
    }




}