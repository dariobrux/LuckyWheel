package rubikstudio.library

import rubikstudio.library.model.LuckyItem

interface OnItemRotationListener {
    fun onItemRotated(luckyItem: LuckyItem)
}