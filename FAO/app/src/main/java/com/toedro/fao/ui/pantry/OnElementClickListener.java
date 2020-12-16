package com.toedro.fao.ui.pantry;
/**
 * interface that provides both the functioning of the edit button and delete button
 */
public interface OnElementClickListener {
    void onElementEditClick(PantryListData data);
    void onElementDeleteClick(PantryListData data);
}
