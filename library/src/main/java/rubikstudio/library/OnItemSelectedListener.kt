package rubikstudio.library

import rubikstudio.library.model.LuckyItem

/**
 * Created by Dario Bruzzese
 * with the collaboration of Paolo Vernazza and Luca Lorrai
 * on 10/07/2018
 * at Pillo Labs Srl.
 */
interface OnItemSelectedListener {

    fun onItemSelected(luckyItem: LuckyItem)
}