package wilquer.lima.desafioconcrete.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repository(
        val id: Int,
        val full_name: String,
        val name: String,
        val description: String,
        val owner: Owner,
        val stargazers_count: Int,
        val forks: Int
) : Parcelable