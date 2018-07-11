package rubikstudio.library

import rubikstudio.library.model.LuckyItem

interface OnItemRotatedListener {
    fun onItemRotated(luckyItem: LuckyItem)
}