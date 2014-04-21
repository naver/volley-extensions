package com.navercorp.volleyextensions.view;
/**
 * An interface for restoring data and saving.
 * @param <T> Specific type to be restored
 */
interface Restorable<T> {
	/**
	 * @param value data to be restored 
	 */
	void restore(T value);
	/**
	 * @return data to be saved
	 */
	T save();
}
