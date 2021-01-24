package kktyu.xyz.testphotoviewer.ui.detail

import kktyu.xyz.testphotoviewer.Url

data class PhotoDetailModel(
    var title: String,
    var description: String,
    var date: String,
    var url: Url
)
