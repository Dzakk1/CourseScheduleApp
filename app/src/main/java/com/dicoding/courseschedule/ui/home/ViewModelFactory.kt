package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.data.DataRepository
import java.lang.reflect.InvocationTargetException

class ViewModelFactory(private val repository: DataRepository?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(DataRepository::class.java).newInstance(repository)
        }
        catch (e: InstantiationException) {
            throw RuntimeException("Error create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Error create an instance of $modelClass", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Error create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Error create an instance of $modelClass", e)
        }
    }

    companion object {
        fun createFactory(activity: HomeActivity) : ViewModelFactory {
            val context = activity.applicationContext ?: throw IllegalStateException("Not yet attached to Application")
            return ViewModelFactory(DataRepository.getInstance(context))
        }
    }
}